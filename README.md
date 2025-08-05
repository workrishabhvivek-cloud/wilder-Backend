<h1>E-Commerce Web Application: Wilder</h1>

<h2>📖 Overview</h2>
<p>Wilder is an online e-commerce platform built with <strong>Spring Boot</strong>, <strong>Java 21</strong>, and <strong>Gradle</strong>. Designed for outdoor and hunting gear enthusiasts, Wilder offers a seamless shopping experience with product catalog management, user authentication, shopping cart, and order processing.</p>

<h2>🛠️ Tech Stack</h2>
<ul>
  <li><strong>Backend:</strong> Spring Boot (Java 21)</li>
  <li><strong>Build Tool:</strong> Gradle</li>
  <li><strong>Database:</strong> MySQL (AWS RDS)</li>
  <li><strong>ORM:</strong> Spring Data JPA / Hibernate</li>
  <li><strong>Security:</strong> Spring Security (JWT-based authentication)</li>
  <li><strong>API:</strong> RESTful endpoints</li>
  <li><strong>Testing:</strong> JUnit 5, Mockito</li>
</ul>

<h2>🔑 Key Features</h2>
<ul>
  <li>User login (JWT authentication)</li>
  <li>Product catalog CRUD operations</li>
  <li>Category management</li>
  <li>Shopping cart &amp; checkout flow</li>
  <li>Order history &amp; tracking</li>
  <li>Role-based access control (ADMIN vs. CUSTOMER)</li>
  <li>Input validation &amp; error handling</li>
  <li>Pagination &amp; sorting for product listings</li>
</ul>

<h2>📋 Prerequisites</h2>
<ul>
  <li>Java 21 SDK</li>
  <li>Gradle (wrapper provided)</li>
  <li>MySQL 8.x (or compatible)</li>
  <li>Git</li>
</ul>

<h2>⚙️ Configuration</h2>
<ol>
  <li><strong>Clone the repository</strong>
    <pre><code>git clone https://github.com/workrishabhvivek-cloud/wilder-Backend.git
cd wilder-Backend</code></pre>
  </li>
</ol>

<h2>📦 API Endpoints</h2>
<table>
  <thead>
    <tr><th>Method</th><th>Endpoint</th><th>Description</th></tr>
  </thead>
  <tbody>
    <tr><td>POST</td><td><code>/api/auth/login</code></td><td>User login (returns JWT token)</td></tr>
    <tr><td>GET</td><td><code>/api/products</code></td><td>List all products</td></tr>
    <tr><td>GET</td><td><code>/api/products/{id}</code></td><td>Get product by ID</td></tr>
    <tr><td>POST</td><td><code>/api/products</code></td><td>Create a new product (ADMIN)</td></tr>
    <tr><td>PUT</td><td><code>/api/products/{id}</code></td><td>Update product (ADMIN)</td></tr>
    <tr><td>DELETE</td><td><code>/api/products/{id}</code></td><td>Delete product (ADMIN)</td></tr>
    <tr><td>POST</td><td><code>/api/cart</code></td><td>Add to cart</td></tr>
    <tr><td>GET</td><td><code>/api/cart</code></td><td>View cart</td></tr>
    <tr><td>POST</td><td><code>/api/orders</code></td><td>Place an order</td></tr>
    <tr><td>GET</td><td><code>/api/orders</code></td><td>View user orders</td></tr>
  </tbody>
</table>
<p><em>✏️ Add more endpoints as implemented.</em></p>

<h2>🗂️ Project Structure</h2>
<pre><code>src
├── main
│   ├── java
│   │   └── com.wilderBackend
│   │       ├── config       # Security, etc.
│   │       ├── auth         # authentication folder
│   │       ├── entity       # JPA entities
│   │       ├── repository   # Spring Data interfaces
│   │       ├── service      # Business logic
│   │       └── utils        # JWT utils, mappers
│   └── resources
│       └── application.properties
└── test
    └── java
        └── com.wilderBackend
            └── (unit & integration tests)</code></pre>

<h2>🤝 Contributing</h2>
<p>Contributions are welcome! Please:</p>
<ol>
  <li>Fork the repo</li>
  <li>Create a feature branch</li>
  <li>Commit your changes</li>
  <li>Push and open a PR</li>
</ol>

<blockquote>
  <p>Built with ❤️ and Spring Boot 🚀</p>
</blockquote>
