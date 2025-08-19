package com.wilderBackend.download;

import com.opencsv.CSVWriter;
import com.wilderBackend.security.filter.AsyncSecurityContextPropagationFilter;
import com.wilderBackend.security.nonCloseableOutputStream.NonClosableOutputStream;
import com.wilderBackend.security.userSession.entity.UserSession;
import com.wilderBackend.security.userSession.repository.UserSessionRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/api/download")
@RequiredArgsConstructor
@Slf4j
public class DownloadController {

    private final UserSessionRepository userSessionRepository;

    @GetMapping(value = "/file", produces = "text/csv")
    public ResponseEntity<StreamingResponseBody> downloadFile(HttpServletRequest request) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authentication);
        request.setAttribute(AsyncSecurityContextPropagationFilter.ATTR, securityContext);

        //Build streaming body
        StreamingResponseBody stream = outputStream -> {
            // Ensure the streaming thread has the security context set while writing
            SecurityContext previous = SecurityContextHolder.getContext();
            try {
                SecurityContextHolder.setContext(securityContext);

                // Wrap servlet output so closing the CSV csvWriter doesn't close servlet stream
                NonClosableOutputStream nonClosableOutputStream = new NonClosableOutputStream(outputStream);
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(nonClosableOutputStream, StandardCharsets.UTF_8);
                CSVWriter csvWriter = new CSVWriter(outputStreamWriter);

                try {
                    // Header
                    csvWriter.writeNext(new String[]{"ID", "User Id", "Expires At", "Status", "Session Id", "Created At", "Created By"});
                    csvWriter.flush();

                    int page = 0;
                    final int size = 100_000;
                    List<UserSession> batch;

                    // Loop pages until empty
                    do {
                        batch = userSessionRepository.findAllStreamList(PageRequest.of(page, size));
                        if (batch == null || batch.isEmpty()) {
                            break;
                        }
                        for (UserSession entity : batch) {
                            csvWriter.writeNext(new String[]{
                                    entity.getId() == null ? "" : String.valueOf(entity.getId()),
                                    entity.getUserId() == null ? "" : String.valueOf(entity.getUserId()),
                                    entity.getExpiresAt() == null ? "" : entity.getExpiresAt().toString(),
                                    entity.getStatus() == null ? "" : entity.getStatus(),
                                    entity.getSessionId() == null ? "" : entity.getSessionId(),
                                    entity.getCreatedAt() == null ? "" : entity.getCreatedAt().toString(),
                                    entity.getCreatedBy() == null ? "" : entity.getCreatedBy().toString()
                            });
                        }
                        // flush each page so client receives progressive chunks
                        csvWriter.flush();
                        page++;
                    } while (!batch.isEmpty());

                    // final flush
                    csvWriter.flush();
                } catch (IOException ioe) {
                    // client probably disconnected — stop quietly, but log for debugging
                    log.info("Client disconnected during streaming: {}", ioe.getMessage());
                } finally {
                    // Don't close the CSVWriter (closing would close the servlet stream).
                    // We flush to be safe; NonClosableOutputStream prevents underlying close.
                    try { csvWriter.flush(); } catch (Exception ignored) {}
                }
            } finally {
                // Restore previous security context on this thread
                SecurityContextHolder.setContext(previous);
            }
        };

        // Return response
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"data.csv\"")
                .contentType(MediaType.parseMediaType("text/csv; charset=UTF-8"))
                .body(stream);
    }

}
