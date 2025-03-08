from concurrent import futures
import grpc

from analyser_service import math_expectation
from analytics_api_pb2 import AnalyticsResult
from analytics_api_pb2_grpc import DataProcessingServicer, add_DataProcessingServicer_to_server


class DataProcessingService(DataProcessingServicer):
    def ProcessNumbers(self, request, context):
        return AnalyticsResult(result=math_expectation(request.numbers))


def serve():
    server = grpc.server(futures.ThreadPoolExecutor(max_workers=20))  # Увеличено до 20
    add_DataProcessingServicer_to_server(DataProcessingService(), server)
    server.add_insecure_port('localhost:9999')

    try:
        server.start()
        print("Server is running on port 9999...")
        server.wait_for_termination()  # Ожидаем завершения работы сервера
    except Exception as e:
        print(f"Error starting server: {e}")


if __name__ == '__main__':
    serve()  # Запускаем сервер
