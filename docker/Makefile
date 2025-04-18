# Указываем файл .env
ENV_FILE = .env
ifneq ("$(wildcard $(ENV_FILE))","")
    include $(ENV_FILE)
endif

start-db:
	@echo "Starting database services..."
	docker compose \
		--project-name invest-bot \
		-f docker-compose.yml \
		up \
		invest-orchestrator-db invest-orchestrator-flyway \
		-d

stop-db:
	@echo "Stopping and removing database services..."
	docker compose \
		-f docker-compose.yml \
		down \
		--volumes

.PHONY: start-db stop-db