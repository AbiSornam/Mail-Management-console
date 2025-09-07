import controller.MailController;
import model.MailRepository;
import view.MailView;

public class App {
    public static void main(String[] args) {
        MailRepository repository = new MailRepository();
        MailView view = new MailView();
        MailController controller = new MailController(repository, view);
        controller.run();
    }
}
