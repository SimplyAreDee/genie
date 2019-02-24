package click.acme.genius.Utils;

import java.util.ArrayList;
import click.acme.genius.Models.Question;

public class FakeQuestionList {
    public static ArrayList<Question> getFakeQuestionList(int number) {
        ArrayList<Question> fakeQuestions = new ArrayList<>();
        Question question;
        for (int i = 1; i < number; i++) {
            question = FakeQuestionList.generateFakeQuestion(i);
            fakeQuestions.add(question);
        }

        return fakeQuestions;
    }

    private static Question generateFakeQuestion(int index) {
        Question question = new Question();

        question.setTitle("Titre " + index);
        question.setPage("Page " + index);
        question.setNumber("Numero" + index);
        question.setSubject("Français");
        question.setInstruction("Doit avoir au moins 3 paragraphes");

        return question;
    }
}
