# Nekki
The application monitors the input directory for files in specified format (see <a href ="https://github.com/shtykh/Nekki#input-file-format">"Input file format" section</a>) and saves its content to a database.

#Disposition
The application requires:<br>
1) PostgreSQL installed<br>
2) *postgres* database and *postgres* superuser in there<br>
3) PostgreSQL bin directory added to system PATH variable e.g.<br>
<code>set PATH=C:\Program Files\PostgreSQL\9.4\bin;%PATH%</code> in windows command line or<br>
<code>export PATH=/Applications/Postgres.app/Contents/MacOS/bin:$PATH</code> in unix command line.<br>
4) PostgreSQL server running at localhost's standart port (5432)

#Installation
1) Download executable jar <a href="https://github.com/shtykh/Nekki/releases/download/v.1.0/Nekki-1.0.jar"> from here</a><br>
2) Extract *init.sh*(*init.bat* in windows) file from the jar. Or download it <a href="https://github.com/shtykh/Nekki/tree/master/src/main/resources">from here</a><br>
3) Run *init.sh*(*init.bat* in windows) to create user *shtykhnekki_user*, database *shtykhnekki* and table *entry* in it for application needs (I really hope you did not name objects in your database so strange).<br>
4) You are ready to launch jar via <code>java -jar jarname.jar<code><br>

# Input file format
```xml
<Entry>
  <!-- Content is a string no longer then 1024 -->
  <content>Ignota similique persequeris eos ne.</content>
  <!-- Date in DATE_FORMAT -->
  <creationDate>2014-01-01 00:00:00</creationDate>
</Entry>
```
You can change *DATE_FORMAT* in nekki.properties (see below).
# Properties
There are two *.properties* files inside the jar: *log4j.properties* (see http://logging.apache.org/log4j/1.2/manual.html) and *nekki.properties* (see in table below). You are free to change any parameter.

| Name | Description          | Default value|
| ------------- | ----------- |--------------|
| THREAD_POOL_SIZE      | Number of Threads that will parse xml files simultaniously| 20|
| DATE_FORMAT     | Format for *creationDate* value in input files | yyyy-MM-dd HH:mm:ss|
| INPUT_PATH     | Path for input directory    | files/input/|
| DONE_PATH     | Path for processed successfully files directory     | files/done/|
| BAD_PATH     | Path for processed unsuccessfully files directory      | files/bad/|
