package ru.nsu.fit.wheretogo.service;

import org.springframework.core.io.Resource;//для работы с ресурсами - фрагментами данных
import org.springframework.core.io.UrlResource;//используется для доступа к ресурсам по его url
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;////spring boot юзает для передачи файлов от сервера к клиенту, те тот, который приходит с запросов
import ru.nsu.fit.wheretogo.entity.Place;
import ru.nsu.fit.wheretogo.entity.PlacePicture;
import ru.nsu.fit.wheretogo.repository.PicturesRepository;
import ru.nsu.fit.wheretogo.repository.PlaceRepository;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;////утилитный класс для работы с файлами, локальными!
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

//Path normalize() — «нормализует» текущий путь, удаляя из него ненужные элементы. При обозначении путей часто используются символы “.” (“текущая директория”) и “..” (родительская директория). Например: “./Pictures/dog.jpg” обозначает, что в той директории, в которой мы сейчас находимся, есть папка Pictures, а в ней — файл “dog.jpg”
//Так вот. Если в твоей программе появился путь, использующий “.” или “..”, метод normalize() позволит удалить их и получить путь, в котором они не будут содержаться:
//Вместо единого класса File появились целых 3 класса: Paths, Path и Files
//а с помощью files - управляем файлами - создание, удаление файлов/директорий
//Paths — это совсем простой класс с единственным статическим методом get(). Его создали исключительно для того, чтобы из переданной строки или URI получить объект типа Path.
@Service
public class PlacePicturesService {
    private final PicturesRepository picturesRepository;
    private final Path uploadDirectory;////нужен для работы с с файлами, интерфейс

    public PlacePicturesService(PicturesRepository picturesRepository) {
        this.picturesRepository = picturesRepository;
        Path uploadDirectory;
        try {
            uploadDirectory = Paths.get( "/data/pictures").toAbsolutePath().normalize();////с помощью функции get получаем объект, инкапсулирующий информацию о пути к директории, преобразуем путь к абсолютному - те указываем точное местарасположение
            Files.createDirectory(uploadDirectory);////создаем директорию для хранения файлов - картинок
        } catch (IOException exception) {
            uploadDirectory = null;
        }
        this.uploadDirectory = uploadDirectory;
    }

    public void savePicture(MultipartFile file, Long placeId) throws Exception {
        Path picFile = uploadDirectory.resolve(placeId.toString() + "_" + file.getName());////resolve - используется для преобразования заданной строки пути в Path
        try {
            Files.copy(file.getInputStream(), picFile, StandardCopyOption.REPLACE_EXISTING);////копируем файл file.getInputStream() в picFile
        } catch (IOException exception) {
            throw new Exception("Can't upload a picture");
        }
        picturesRepository.save(new PlacePicture().setPlace(new Place().setId(placeId)).setUrl(picFile.toString()));
    }

    public Resource loadPicture(String url) throws Exception {
        try {
            Resource resource = new UrlResource(uploadDirectory.resolve(url).normalize().toUri());////получаем url/uri картинки из директории
            if (resource.exists()) {
                return resource;////если существует - возвращаем ее, иначе исключение
            } else {
                throw new FileNotFoundException("File " + url + " hasn't been found");
            }
        } catch (MalformedURLException e) {
            throw new FileNotFoundException("File " + url + " hasn't been found");
        }
    }
}
