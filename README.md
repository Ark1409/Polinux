# Polinux
Extendable, configurable HTTP web server coded from scratch

Implemented features include:
- **Simple File Retrieval**: Serve static text files from the file system, with URLs mapped to the directory hierarchy.
  
- **HTTP/HTTPS Support**: Configurable to run on both HTTP and HTTPS, with simple SSL/TLS integration for secure communication.

- **Plugin API**: Allows developers to create custom servlets mapped to specific URL patterns, offering full control over request handling and server behavior.

- **Experimental Command API**: Enables dynamic server management during runtime, allowing configuration changes and administrative tasks without restarting the server.

- **Easy YAML Configuration**: The server is configured using a clear, human-readable YAML file, making it simple to define settings such as ports, file paths, and servlet mappings.
