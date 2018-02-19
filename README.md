Documentation:
If you want run application, you need three things: mysql system, maven and configured database. 
It is simple, step-by-step instruction, which help you in configuration process.

1. Install and configure mysql system.

1a. If you already have mysql system on your computer, you can skip 1b and 1c steps.

1b. Installation may be different in each kind of operation system. Command, which install mysql system for linux: sudo apt-get install mysql -server.

1c. When you will be asked for password in installation process, please type: contacts.

1d. Run mysql console with command "mysql -u root -p" and type your password.

1e. To create new database for project, use command "create database contacts;" in opened mysql console.

1f. Create new user "create user 'springuser'@'localhost' identified by 'contacts';". If you did not choose "contacts" as a password, type your own password in this place.

1e. Add permissions to 'contacts' database for new user with command "grant all on contacts.* to 'springuser'@'localhost';".

1g. If you already have mysql system or you chosen other user or password, you should edit file /src/main/resources/application.properties in project folder. In this file you must change values of properties: spring.datasource.username, spring.datasource.password.

2. Install maven.

2a. Installation may be different in each kind of operation system. Command, which install maven for linux: sudo apt-get install maven.

3. Run the project.

3a. Go to project folder and use command: mvn spring-boot:run.

3b. You have opened application and you can send requests on url address /localhost:8080.

Dokumentacja:
Jeżeli chcesz uruchomić aplikacje, potrzebujesz trzech rzeczy: systemu mysql, programu maven i skonfigurowanej bazy danych.
To jest prosta instrukcja, która krok po kroku pomoże ci w procesie konfiguracji.

1. Zainstaluj i skonfiguruj system mysql.

1a. Jeżeli posiadasz już system mysql na swoim komputerze, możesz pominąć kroki 1b i 1c.

1b. Instalacja może być rózna dla każdego typu systemu operacyjnego. Komenda, która instaluje system mysql dla linuxa to: sudo apt-get install mysql -server.

1c. Gdy zostaniesz zapytany o hasło w procesie instalacji, proszę wpisz: contacts.

1d. Uruchom konstole mysql używając komendy "mysql -u root -p" i wpisz swoje hasło.

1e. Stwórz nową bazę danych dla projektu używając komendy "create database contacts;" w konsoli otwartej konsoli mysql.

1f. Stwórz nowego użytkownika "create user 'springuser'@'localhost' identified by 'contacts';". Jeżeli nie wybrałeś "contacts" jako hasła wpisz swoje własne hasło w tym miejscu.

1e. Dodaj uprawnienia do bazy danych 'contacts' dla nowego użytkownika używając komendy "grant all on contacts.* to 'springuser'@'localhost';".

1g. Jeżeli posiadasz już system mysql lub wybrałeś inną nazwę użytkownika lub hasła, powinieneś edytować plik /src/main/resources/application.properties w folderze projektu. W tym pliku musisz zmienić wartości dla właściwości: spring.datasource.username, spring.datasource.password.

2. Zainstaluj maven.

2a. Instalacja może być różna dla każdego typu systemu operacyjnego. Komenda, która instaluje maven dla linux to: sudo apt-get install maven.

3. Uruchom projekt.

3a. Przejdź do folderu projektu i użyj komendy: mvn spring-boot:run.

3b. Posiadasz otwartą aplikację i możesz wysyłać żądania na adres url /localhost:8080.
