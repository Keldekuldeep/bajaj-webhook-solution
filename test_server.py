from http.server import HTTPServer, BaseHTTPRequestHandler
import json

class MockHandler(BaseHTTPRequestHandler):

    def log_message(self, format, *args):
        print(f"[SERVER] {self.address_string()} - {format % args}")

    def do_POST(self):
        content_length = int(self.headers.get('Content-Length', 0))
        body = self.rfile.read(content_length).decode('utf-8')

        print(f"\n[REQUEST] POST {self.path}")
        print(f"[HEADERS] {dict(self.headers)}")
        print(f"[BODY] {body}")

        if '/generateWebhook' in self.path:
            response = {
                "webhook": "http://localhost:9090/hiring/testWebhook/JAVA",
                "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ0ZXN0In0.test_token"
            }
            self.send_response(200)
            self.send_header('Content-Type', 'application/json')
            self.end_headers()
            self.wfile.write(json.dumps(response).encode())
            print(f"[RESPONSE] Sent webhook + accessToken")

        elif '/testWebhook' in self.path:
            auth = self.headers.get('Authorization', 'MISSING')
            print(f"[AUTH] Authorization header: {auth}")
            try:
                data = json.loads(body)
                print(f"[ANSWER] finalQuery received:\n  {data.get('finalQuery', 'N/A')}")
            except Exception:
                print(f"[ANSWER] Raw body: {body}")

            response = {"message": "Answer submitted successfully", "status": "ok"}
            self.send_response(200)
            self.send_header('Content-Type', 'application/json')
            self.end_headers()
            self.wfile.write(json.dumps(response).encode())
            print(f"[RESPONSE] Submission accepted")

        else:
            self.send_response(404)
            self.end_headers()

if __name__ == '__main__':
    server = HTTPServer(('localhost', 9090), MockHandler)
    print("Mock server running on http://localhost:9090")
    print("Endpoints:")
    print("  POST /hiring/generateWebhook/JAVA  -> returns webhook + accessToken")
    print("  POST /hiring/testWebhook/JAVA      -> accepts final SQL answer")
    print("\nWaiting for requests...\n")
    server.serve_forever()
