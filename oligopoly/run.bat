@echo off

if exist bin (rd /s /q bin)

javac --module-path ".\lib\javafx\lib" --add-modules javafx.controls,javafx.fxml -d bin src/*.java src/board/*.java src/fxml/*.java src/player/*.java src/tile/*.java src/state/*.java
java --module-path ".\lib\javafx\lib" --add-modules javafx.controls,javafx.fxml -cp bin Main

pause
