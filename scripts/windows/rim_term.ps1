# 1. Use the folder where this script is saved
$JarDir = $PSScriptRoot

# 2. Save the session init script in Temp
$SessionScriptPath = Join-Path $env:TEMP "rim-terminal-session.ps1"

$SessionScript = @"
Set-Location -Path "$JarDir"
`$Host.UI.RawUI.WindowTitle = "rim tool"

function global:prompt {
    "RIM> "
}

function global:rim {
    `$JarFile = Get-ChildItem -Path "$JarDir" -Filter "rim*.jar" | 
               Sort-Object LastWriteTime -Descending | 
               Select-Object -First 1

    if (`$JarFile) {
        Write-Host "Running: `$(`$JarFile.Name)" -ForegroundColor Cyan
        java -jar "`$(`$JarFile.FullName)" @args
    } else {
        Write-Error "No matching rim*.jar file found in $JarDir"
    }
}

`$InitialJar = Get-ChildItem -Path "$JarDir" -Filter "rim*.jar" | Sort-Object LastWriteTime -Descending | Select-Object -First 1
if (`$InitialJar) {
    Write-Host "Command 'rim' mapped to: `$(`$InitialJar.Name)" -ForegroundColor Green
} else {
    Write-Host "Warning: No rim*.jar found in $JarDir" -ForegroundColor Yellow
}
"@

Set-Content -Path $SessionScriptPath -Value $SessionScript -Encoding UTF8

# 3. Register/Update the "rim tool" profile in Windows Terminal settings.json
$WtSettingsPaths = @(
    "$env:LOCALAPPDATA\Packages\Microsoft.WindowsTerminal_8wekyb3d8bbwe\LocalState\settings.json",
    "$env:LOCALAPPDATA\Packages\Microsoft.WindowsTerminalPreview_8wekyb3d8bbwe\LocalState\settings.json",
    "$env:LOCALAPPDATA\Microsoft\Windows Terminal\settings.json"
)

$SettingsFile = $WtSettingsPaths | Where-Object { Test-Path $_ } | Select-Object -First 1

if ((Get-Command wt.exe -ErrorAction SilentlyContinue) -and $SettingsFile) {
    # Strip // comments if present before parsing JSON
    $RawJson = Get-Content -Path $SettingsFile -Raw
    $CleanJson = $RawJson -replace '(?m)^\s*//.*$', ''
    $Settings = $CleanJson | ConvertFrom-Json

    $ProfileGuid = "{e7a1c9b0-4f2a-4d8e-9b1c-3f5a7e9d2c1b}"
    $CommandLine = "powershell.exe -NoExit -ExecutionPolicy Bypass -File `"$SessionScriptPath`""

    $RimProfile = [PSCustomObject]@{
        guid                    = $ProfileGuid
        name                    = "rim tool"
        commandline             = $CommandLine
        startingDirectory       = $JarDir
        tabTitle                = "rim tool"
        suppressApplicationTitle = $true
        hidden                  = $false
    }

    # Update existing profile or add a new one
    $ExistingIndex = -1
    for ($i = 0; $i -lt $Settings.profiles.list.Count; $i++) {
        if ($Settings.profiles.list[$i].guid -eq $ProfileGuid -or $Settings.profiles.list[$i].name -eq "rim tool") {
            $ExistingIndex = $i
            break
        }
    }

    if ($ExistingIndex -ge 0) {
        $Settings.profiles.list[$ExistingIndex] = $RimProfile
    } else {
        $Settings.profiles.list += $RimProfile
    }

    $Settings | ConvertTo-Json -Depth 32 | Set-Content -Path $SettingsFile -Encoding UTF8

    # Launch Windows Terminal using the named profile (-p)
    Start-Process wt.exe -ArgumentList '-p', '"rim tool"'
} else {
    # Fallback for classic PowerShell if Windows Terminal isn't installed
    Start-Process powershell.exe -WorkingDirectory $JarDir -ArgumentList '-NoExit', '-ExecutionPolicy', 'Bypass', '-File', "`"$SessionScriptPath`""
}
