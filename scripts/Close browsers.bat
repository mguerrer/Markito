echo Borra drivers
echo Marcos Guerrero. 15-11-2020

REM Genera web drivers.
echo Creando Selenium drivers
cd c:\set\ejemplos\calculadora\
tasklist | find /i "chromedriver.exe" && taskkill /im chromedriver.exe /F || echo Proceso "chromedriver.exe" no se está ejecutando.
tasklist | find /i "chrome.exe" && taskkill /im chrome.exe /F || echo Proceso "chrome.exe" no se está ejecutando.
tasklist | find /i "msedgedriver.exe" && taskkill /im msedgedriver.exe /F || echo Proceso "msedgedriver.exe" no se está ejecutando.
tasklist | find /i "msedge.exe" && taskkill /im msedge.exe /F || echo Proceso "msedge.exe" no se está ejecutando.
tasklist | find /i "chromedriver.exe" && taskkill /im chromedriver.exe /F || echo Proceso "chromedriver.exe" no se está ejecutando.
tasklist | find /i "chrome.exe" && taskkill /im chrome.exe /F || echo Proceso "chrome.exe" no se está ejecutando.
tasklist | find /i "IEDriverServer.exe" && taskkill /im IEDriverServer.exe /F || echo Proceso "IEDriverServer.exe" no se está ejecutando.
tasklist | find /i "iexplore.exe" && taskkill /im "iexplore.exe" /F || echo Proceso "iexplore.exe" no se está ejecutando.


pause "Presione cualquier tecla!"
