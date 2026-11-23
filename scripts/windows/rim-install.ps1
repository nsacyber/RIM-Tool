# Resolve paths dynamically
$DesktopPath = [Environment]::GetFolderPath("Desktop")
$ShortcutPath = Join-Path -Path $DesktopPath -ChildPath "rim-tool.lnk"
$TargetScript = Join-Path -Path $PWD.Path -ChildPath "rim_term.vbs"

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