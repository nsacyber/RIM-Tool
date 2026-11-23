# Resolve paths dynamically
$DesktopPath = [Environment]::GetFolderPath("Desktop")
$ShortcutPath = Join-Path -Path $DesktopPath -ChildPath "rim-tool.lnk"
$TargetScript = Join-Path -Path $PWD.Path -ChildPath "rim_term.vbs"

# Capture the current working directory and the directory where the script resides
$currentDir = (Get-Location).Path
$scriptDir = $PSScriptRoot

# Verify the script is being called from the directory it is stored in
if ($currentDir -ne $scriptDir) {
    Write-Host "Deletion canceled: This script must be run from the directory to be deleted." -ForegroundColor Red
    Write-Host "Please run the following command first, then try again:" -ForegroundColor Yellow
    Write-Host "cd `"$scriptDir`"" -ForegroundColor Cyan
    exit
}

# Create the shortcut object
$WshShell = New-Object -ComObject WScript.Shell
$Shortcut = $WshShell.CreateShortcut($ShortcutPath)

# Configure shortcut properties
$Shortcut.TargetPath = "wscript.exe"
$Shortcut.Arguments = " `"$TargetScript`""
#$Shortcut.TargetPath = "powershell.exe"
#$Shortcut.Arguments = "-NoProfile -ExecutionPolicy Bypass -File `"$TargetScript`""
$Shortcut.WorkingDirectory = $PWD.Path
$Shortcut.IconLocation = "powershell.exe,0"

# Save the shortcut to the Desktop
$Shortcut.Save()