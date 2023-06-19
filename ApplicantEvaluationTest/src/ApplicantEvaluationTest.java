import java.util.Scanner;

/**
 * I partial exam 2016
 */
public class ApplicantEvaluationTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String name = scanner.nextLine();
        int creditScore = scanner.nextInt();
        int employmentYears = scanner.nextInt();
        boolean hasCriminalRecord = scanner.nextBoolean();
        int choice = scanner.nextInt();
        Applicant applicant = new Applicant(name, creditScore, employmentYears, hasCriminalRecord);
        Evaluator.TYPE type = Evaluator.TYPE.values()[choice];
        Evaluator evaluator = null;
        try {
            evaluator = EvaluatorBuilder.build(type);
            System.out.println("Applicant");
            System.out.println(applicant);
            System.out.println("Evaluation type: " + type.name());
            if (evaluator.evaluate(applicant)) {
                System.out.println("Applicant is ACCEPTED");
            } else {
                System.out.println("Applicant is REJECTED");
            }
        } catch (InvalidEvaluation invalidEvaluation) {
            System.out.println("Invalid evaluation");
        }
    }
}

class Applicant {
    private String name;
    private int creditScore;
    private int employmentYears;
    private boolean hasCriminalRecord;

    public Applicant(String name, int creditScore, int employmentYears, boolean hasCriminalRecord) {
        this.name = name;
        this.creditScore = creditScore;
        this.employmentYears = employmentYears;
        this.hasCriminalRecord = hasCriminalRecord;
    }

    public String getName() {
        return name;
    }

    public int getCreditScore() {
        return creditScore;
    }

    public int getEmploymentYears() {
        return employmentYears;
    }

    public boolean hasCriminalRecord() {
        return hasCriminalRecord;
    }

    @Override
    public String toString() {
        return String.format("Name: %s\nCredit score: %d\nExperience: %d\nCriminal record: %s\n",
                name, creditScore, employmentYears, hasCriminalRecord ? "Yes" : "No");
    }
}

interface Evaluator {
    enum TYPE {
        NO_CRIMINAL_RECORD,
        MORE_EXPERIENCE,
        MORE_CREDIT_SCORE,
        NO_CRIMINAL_RECORD_AND_MORE_EXPERIENCE,
        MORE_EXPERIENCE_AND_MORE_CREDIT_SCORE,
        NO_CRIMINAL_RECORD_AND_MORE_CREDIT_SCORE,
        INVALID // should throw exception
    }
    boolean evaluate(Applicant applicant);
}
class EvaluatorBuilder {
    public static Evaluator build(Evaluator.TYPE type) throws InvalidEvaluation {
        switch (type){
            case NO_CRIMINAL_RECORD:
                return new noCriminalRecord();
            case MORE_EXPERIENCE:
                return new moreExperience();
            case MORE_CREDIT_SCORE:
                return new moreCreditScore();
            case NO_CRIMINAL_RECORD_AND_MORE_EXPERIENCE:
                return new noCrimeMoreExperience();
            case MORE_EXPERIENCE_AND_MORE_CREDIT_SCORE:
                return new moreExperienceMoreCredit();
            case NO_CRIMINAL_RECORD_AND_MORE_CREDIT_SCORE:
                return new noCrimeMoreCredit();
            default: throw new InvalidEvaluation();
        }
    }
}
class noCriminalRecord implements Evaluator {
    @Override
    public boolean evaluate(Applicant applicant) {
        return !applicant.hasCriminalRecord();
    }
}
class moreExperience implements Evaluator {
    @Override
    public boolean evaluate(Applicant applicant) {
        return applicant.getEmploymentYears() >= 10;
    }
}
class moreCreditScore implements Evaluator {
    @Override
    public boolean evaluate(Applicant applicant) {
        return applicant.getCreditScore() >= 500;
    }
}
class noCrimeMoreExperience implements Evaluator {
    @Override
    public boolean evaluate(Applicant applicant) {
        return applicant.getEmploymentYears() >= 10 && !applicant.hasCriminalRecord();
    }
}
class moreExperienceMoreCredit implements Evaluator {
    @Override
    public boolean evaluate(Applicant applicant) {
        return applicant.getEmploymentYears() >= 10 && applicant.getCreditScore() >= 500;
    }
}
class noCrimeMoreCredit implements Evaluator {
    @Override
    public boolean evaluate(Applicant applicant) {
        return !applicant.hasCriminalRecord() && applicant.getCreditScore() >= 500;
    }
}
class InvalidEvaluation extends Exception{

}
