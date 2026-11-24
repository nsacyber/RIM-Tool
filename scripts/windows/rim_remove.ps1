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

# Define the desktop shortcut path
$desktopPath = [Environment]::GetFolderPath("Desktop")
$shortcutPath = Join-Path -Path $desktopPath -ChildPath "rim-tool.lnk"

# Display targets and prompt the user for confirmation
Write-Host "WARNING: This action permanently deletes files without using the Recycle Bin." -ForegroundColor Yellow
Write-Host "Directory to delete : $currentDir"
Write-Host "Shortcut to delete  : $shortcutPath"

$confirmation = Read-Host "Are you sure you want to proceed? (Y/N)"

if ($confirmation -match '^[Yy]$') {
  $targetDir = (Get-Location).Path
  Set-Location ..
  [Environment]::CurrentDirectory = (Get-Location).Path
   Start-Sleep -Milliseconds 200
  Remove-Item -Path $targetDir -Recurse -Force

  # Verify removal
  if (-not (Test-Path -Path $targetDir)) {
    Write-Host "Successfully removed: $targetDir" -ForegroundColor Green
  } else {
     Write-Warning "Failed to completely remove $targetDir. Check for open files or permissions."
  }

    # Delete the desktop shortcut if it exists
    if (Test-Path -LiteralPath $shortcutPath) {
        Remove-Item -LiteralPath $shortcutPath -Force
        Write-Host "Deleted shortcut: $shortcutPath" -ForegroundColor Green
    } else {
        Write-Host "Shortcut not found: $shortcutPath" -ForegroundColor DarkGray
    }
} else {
    Write-Host "Deletion canceled." -ForegroundColor Cyan
}
Write-Host "Press any key to continue..." -NoNewline
$null = [System.Console]::ReadKey($true)
Write-Host ""