# gdrive-downloader

## Building

```bash
mvn package
```

## Configuration

1. Open Web browser, go to "https://developers.google.com/drive/api/v3/quickstart/java#step_1_turn_on_the", click "Enable the Drive API".

2. In resulting dialog, select "Desktop app" and click "CREATE".

3. In resulting dialog, click "DOWNLOAD CLIENT CONFIGURATION" and save the file "credentials.json" to your current working directory.

## Usage

### Downloading files

```bash
java -jar target/gdrive-downloader.jar -fi <file_id> -lf <local_file_path>
```

### Displaying help information

```bash
java -jar target/gdrive-downloader.jar -h
```
