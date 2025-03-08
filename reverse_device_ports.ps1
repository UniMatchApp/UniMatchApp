## Obtén la lista de dispositivos conectados y filtra los IDs válidos
#$devices = adb devices | Select-String "device$" | ForEach-Object { ($_ -split '\s+')[0] }
#
## Verifica si hay dispositivos encontrados
#if (-not $devices) {
#    Write-Host "No devices found." -ForegroundColor Yellow
#    exit
#}
#
## Itera sobre cada dispositivo y aplica los comandos adb reverse
#foreach ($device in $devices) {
#    Write-Host "Applying reverse port forwarding to $device"
#    adb -s $device reverse tcp:3000 tcp:3000
#    adb -s $device reverse tcp:8080 tcp:8080
#    adb -s $device reverse tcp:8081 tcp:8081
#}


# Obtén la lista de dispositivos conectados y filtra los IDs válidos
$devices = adb devices | Select-String "device$" | ForEach-Object { ($_ -split '\s+')[0] }

# Verifica si hay dispositivos encontrados
if (-not $devices) {
    Write-Host "No devices found." -ForegroundColor Yellow
    exit
}

# Función para ejecutar comandos adb con tiempo límite y manejo de errores
function Invoke-AdbCommand {
    param (
        [string]$Device,
        [string]$Command
    )
    try {
        # Ejecuta el comando con un tiempo límite de 10 segundos
        $process = Start-Process -FilePath adb -ArgumentList $Command -NoNewWindow -Wait -PassThru -ErrorAction Stop
        if ($process.ExitCode -eq 0) {
            Write-Host "Command succeeded: adb -s $Device $Command" -ForegroundColor Green
            return $true
        } else {
            Write-Host "Command failed: adb -s $Device $Command" -ForegroundColor Red
            return $false
        }
    } catch {
        Write-Host "Error executing command: adb -s $Device $Command. $_" -ForegroundColor Red
        return $false
    }
}

# Función para reiniciar el servidor ADB
function Reset-AdbServer {
    Write-Host "Resetting adb server..." -ForegroundColor Yellow
    adb kill-server
    adb start-server
    Write-Host "ADB server restarted." -ForegroundColor Green
}

# Itera sobre cada dispositivo y aplica los comandos adb reverse
foreach ($device in $devices) {
    Write-Host "Applying reverse port forwarding to $device"

    # Ejecuta cada comando y verifica si falló
    $success = Invoke-AdbCommand  -Device $device -Command "-s $device reverse tcp:3000 tcp:3000"
    if (-not $success) {
        Reset-AdbServer
        continue # Salta al siguiente dispositivo después de resetear ADB
    }

    $success = Invoke-AdbCommand  -Device $device -Command "-s $device reverse tcp:8080 tcp:8080"
    if (-not $success) {
        Reset-AdbServer
        continue # Salta al siguiente dispositivo después de resetear ADB
    }

    $success = Invoke-AdbCommand  -Device $device -Command "-s $device reverse tcp:8081 tcp:8081"
    if (-not $success) {
        Reset-AdbServer
        continue # Salta al siguiente dispositivo después de resetear ADB
    }
}

