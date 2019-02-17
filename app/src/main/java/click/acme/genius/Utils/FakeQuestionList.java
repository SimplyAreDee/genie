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
        question.setAnswer("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.");
        question.setInstruction("Doit avoir au moins 3 paragraphes");

        return question;
    }
}
