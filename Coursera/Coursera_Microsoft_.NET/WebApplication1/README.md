### How to test via Swagger UI

Authenticate and test endpoints: Navigate to the local URL followed by /swagger (http://localhost:5010/swagger).

Because of the authentication middleware, you must “log in” before making requests:
1. Click the Authorize button at the top right of the Swagger UI.
2. Type the following exactly: `Bearer TechHive-Secret-Token`.
3. Click Authorize, then Close.
