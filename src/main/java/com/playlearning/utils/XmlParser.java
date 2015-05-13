package com.playlearning.utils;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;

public class XmlParser {
//    public static Chapters parseContentsXml() throws JAXBException {
//        JAXBContext jaxbContext = JAXBContext.newInstance(Chapters.class, Chapter.class, SubChapter.class);
//        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
//
//        Chapters chapters = (Chapters) jaxbUnmarshaller.unmarshal(new File("src/main/resources/static/xml/content.xml"));
//
//        return chapters;
//    }
//
//    public static Exercises parseExercisesXml(String subChapterNumber) throws JAXBException {
//        JAXBContext jaxbContext = JAXBContext.newInstance(Exercises.class, Exercise.class);
//        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
//
//        Exercises exercises = (Exercises) jaxbUnmarshaller.unmarshal(new File("src/main/resources/static/xml/" + subChapterNumber + ".xml"));
//
//        return exercises;
//    }
}
