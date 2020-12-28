# the-moneywatcher

This program is a little application to track your transactions in different months f.e. between your salary.
Your data will be saved in a sqlite-database in `config/db/transactions.sqlite`

# Informations

the-moneywatcher is based on JDK 11 and OpenJFX 11. For database stuff it uses hibernate 4.3.11 and sqlite-jdbc 3.34 

# Todo

- [x] add screens
- [x] remove unnecessary code & dependencies
- [ ] add img upload
- [ ] add some tests

# Run

You can run this application over your favorite IDE, just run de.mnreinisch.pp.watcher.Launcher.java
or if you want a jar can build it with `mvn clean install` or only `mvn package` to trigger maven-shade-plugin and create a single jar-file.

# Impressions

## Dashboard

![Dashboard Image](https://i.imgur.com/TzXuuwv.png)
![Dashboard Image negative](https://i.imgur.com/Nexcq0S.png)

## Add transaction
Shortcut from Dashboard: Alt + T + N

![Add Image](https://i.imgur.com/Kxfh8FJ.png)
