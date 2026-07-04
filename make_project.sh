#!/usr/bin/env bash

PROJECT=tabletop-rpg-soundtrack

mkdir -p "$PROJECT"

cd "$PROJECT"

################################################################################
# Root
################################################################################

touch pom.xml
touch Dockerfile
touch docker-compose.yml
touch README.md
touch .gitignore

################################################################################
# Local Storage
################################################################################

mkdir -p local_storage

################################################################################
# Maven
################################################################################

mkdir -p src/main/java/br/org/tabletoprpg/soundtrack
mkdir -p src/main/resources
mkdir -p src/test/java

################################################################################
# Packages
################################################################################

mkdir -p src/main/java/br/org/tabletoprpg/soundtrack/app
mkdir -p src/main/java/br/org/tabletoprpg/soundtrack/controller
mkdir -p src/main/java/br/org/tabletoprpg/soundtrack/model
mkdir -p src/main/java/br/org/tabletoprpg/soundtrack/service
mkdir -p src/main/java/br/org/tabletoprpg/soundtrack/repository
mkdir -p src/main/java/br/org/tabletoprpg/soundtrack/command
mkdir -p src/main/java/br/org/tabletoprpg/soundtrack/strategy
mkdir -p src/main/java/br/org/tabletoprpg/soundtrack/audio
mkdir -p src/main/java/br/org/tabletoprpg/soundtrack/view
mkdir -p src/main/java/br/org/tabletoprpg/soundtrack/exception

################################################################################
# Main
################################################################################

touch src/main/java/br/org/tabletoprpg/soundtrack/Main.java

################################################################################
# Application
################################################################################

touch src/main/java/br/org/tabletoprpg/soundtrack/app/ApplicationContext.java
touch src/main/java/br/org/tabletoprpg/soundtrack/app/Application.java

################################################################################
# Controllers
################################################################################

touch src/main/java/br/org/tabletoprpg/soundtrack/controller/MainController.java

################################################################################
# Models
################################################################################

touch src/main/java/br/org/tabletoprpg/soundtrack/model/Ost.java
touch src/main/java/br/org/tabletoprpg/soundtrack/model/Theme.java
touch src/main/java/br/org/tabletoprpg/soundtrack/model/Playlist.java
touch src/main/java/br/org/tabletoprpg/soundtrack/model/Track.java
touch src/main/java/br/org/tabletoprpg/soundtrack/model/PlayerState.java

################################################################################
# Services
################################################################################

touch src/main/java/br/org/tabletoprpg/soundtrack/service/PlaybackService.java
touch src/main/java/br/org/tabletoprpg/soundtrack/service/ThemeService.java

################################################################################
# Repository
################################################################################

touch src/main/java/br/org/tabletoprpg/soundtrack/repository/ThemeRepository.java
touch src/main/java/br/org/tabletoprpg/soundtrack/repository/LocalStorageThemeRepository.java

################################################################################
# Commands
################################################################################

touch src/main/java/br/org/tabletoprpg/soundtrack/command/Command.java
touch src/main/java/br/org/tabletoprpg/soundtrack/command/CommandDispatcher.java

################################################################################
# Strategy
################################################################################

touch src/main/java/br/org/tabletoprpg/soundtrack/strategy/PlaybackStrategy.java
touch src/main/java/br/org/tabletoprpg/soundtrack/strategy/RandomPlaybackStrategy.java

################################################################################
# Audio
################################################################################

touch src/main/java/br/org/tabletoprpg/soundtrack/audio/AudioPlayer.java
touch src/main/java/br/org/tabletoprpg/soundtrack/audio/DummyAudioPlayer.java
touch src/main/java/br/org/tabletoprpg/soundtrack/audio/AudioPlayerFactory.java

################################################################################
# View
################################################################################

touch src/main/java/br/org/tabletoprpg/soundtrack/view/ConsoleView.java

################################################################################
# Exceptions
################################################################################

touch src/main/java/br/org/tabletoprpg/soundtrack/exception/ApplicationException.java

echo "Project created successfully."



