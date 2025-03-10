default:
  @just --list

# Run a complete development environment
run-dev:
  open-smart-city-home-dev

fmt:
  ./gradlew ktlintCheck
