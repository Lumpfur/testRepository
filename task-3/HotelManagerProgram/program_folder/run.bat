@echo off
chcp 65001
title Hotel Manager

if exist "HotelManagerProgram.jar" (
    echo JAR found! Starting program...
    java -jar HotelManagerProgram.jar
) else (
    echo JAR not found in: %CD%
)
echo end of the program.

pause