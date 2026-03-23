@echo off
set PATH=%PATH%;C:\Program Files\nodejs
cd /d "%~dp0"
echo Installing dependencies...
npm install
echo Installation complete.
pause