# Nekki
The application monitors the input directory for files in specified format (see "Input file format" section) and save its content to a database.

#Instalation
Download <a href="https://github.com/shtykh/Nekki/blob/master/Nekki-1.0.jar">executable jar</a> from project.

The application assumes you have PostgreSQL installed and also have a postgres database and postgres superuser in there.

Use init.sh (init.bat) script inside the jar to create user *shtykhnekki_user*, database *shtykhnekki* and table *entry* in it for application needs. If you allready have it, you gonna have bad time.


# Input file format
```xml
<Entry>
  <!-- Content is a string no longer then MAX_CONTENT_SIZE -->
  <content>
    Ignota similique persequeris eos ne. Cu dicat commune disputando sit, vis laudem fabellas no.
  </content>
  <!-- Date in DATE_FORMAT -->
  <creationDate>
    2014-01-01 00:00:00
  </creationDate>
</Entry>
```
You can change *MAX_CONTENT_SIZE* and *DATE_FORMAT* in nekki.properties (see below).
# Properties
There are two *.properties* files inside the jar: *log4j.properties* (see http://logging.apache.org/log4j/1.2/manual.html) and *nekki.properties* (see in table below). You are free to change any parameter.

| Name | Description          | Default value|
| ------------- | ----------- |--------------|
| THREAD_POOL_SIZE      | Number of Threads which will parse xml files simultaniously| 20|
| DATE_FORMAT     | Format for *creationDate* value in input files | yyyy-MM-dd HH:mm:ss|
| MAX_CONTENT_SIZE     | Maximum allowed symbols number in *content* string in input files | files/input/ |
| INPUT_PATH     | Path for input directory    | files/input/|
| DONE_PATH     | Path for processed successfully files directory     | files/done/|
| BAD_PATH     | Path for processed unsuccessfully files directory      | files/bad/|
