# MCWLauncher
MineCraftWebLauncher - веб-браузер, который может скачивать и запускать игру.
Для загрузки игры и её запуска используется API, которое предоставляется на стороне JavaScript.
Функции API не доступны до того как страница будет полностью загружена, однако JavaScript в браузере уже может быть выполнен, поэтому рекомендуется вызывать данные функции только после полной загрузки страницы.

# JavaScript API
```
launcher.download(String url, String filename)
```
Скачивает файл игры. `url` - URL файла, `filename` -  расположение файла игры
Если файл в папке уже имеется - загрузка не производится

```
launcher.startMC(String player_name, String version, String uuid)
```
Запускает игру из загруженных файлов.
`player_name` - имя игрока, `version` - версия игры(название папки в versions и JSON),`uuid` - UUID игрока.

```
launcher.removeGame()
```
Удаляет все файлы игры

```
launcher.exit() 
```
Закрывает лаунчер

# Сборка
Для самостоятельной сборки необходимо иметь Java 8 (или 7 с доп. библиотеками) и библиотеку GSON.

# TODO
Добавить функции API для работы с окном (перемещение, изменение размера и другое)
Добавить другие способы загрузки файлов (ZIP-архив, BitTorrent и другие)
