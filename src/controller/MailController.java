package controller;

import model.Mail;
import model.MailRepository;
import view.MailView;

import java.util.List;
import java.util.Scanner;

public class MailController {
    private final MailRepository repository;
    private final MailView view;
    private final Scanner scanner;

    public MailController(MailRepository repository, MailView view) {
        this.repository = repository;
        this.view = view;
        this.scanner = new Scanner(System.in);
    }

    public void run() {
        while (true) {
            view.showMenu();
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> storeMail();
                case 2 -> deleteMail();
                case 3 -> addTag();
                case 4 -> showStats();
                case 5 -> searchMail();
                case 6 -> wildcardSearch();
                case 7 -> {
                    System.out.println("👋 Exiting... Goodbye!");
                    return;
                }
                default -> System.out.println("⚠️ Invalid choice. Try again.");
            }
        }
    }

    private void storeMail() {
        System.out.print("Sender: ");
        String sender = scanner.nextLine();
        System.out.print("Receiver: ");
        String receiver = scanner.nextLine();
        System.out.print("Subject: ");
        String subject = scanner.nextLine();
        System.out.print("Content: ");
        String content = scanner.nextLine();

        Mail mail = new Mail(sender, receiver, subject, content);
        repository.addMail(mail);
        System.out.println("✅ Mail stored successfully!");
    }

    private void deleteMail() {
        System.out.print("Enter subject of mail to delete: ");
        String subject = scanner.nextLine();
        if (repository.deleteMail(subject)) {
            System.out.println("🗑️ Mail deleted successfully!");
        } else {
            System.out.println("⚠️ Mail not found.");
        }
    }

    private void addTag() {
        System.out.print("Enter subject of mail to add tag: ");
        String subject = scanner.nextLine();
        List<Mail> results = repository.searchBySubject(subject);
        if (results.isEmpty()) {
            System.out.println("⚠️ Mail not found.");
            return;
        }
        Mail mail = results.get(0);
        System.out.print("Enter tag: ");
        String tag = scanner.nextLine();
        mail.addTag(tag);
        repository.addMail(mail); // Update DB with new tag
        System.out.println("🏷️ Tag added successfully!");
    }

    private void showStats() {
        int total = repository.countMails();
        long spam = repository.countSpamMails();
        view.showStats(total, spam);
    }

    private void searchMail() {
        System.out.print("Enter subject keyword: ");
        String keyword = scanner.nextLine();
        view.displayMails(repository.searchBySubject(keyword));
    }

    private void wildcardSearch() {
        System.out.print("Enter subject pattern (* as wildcard): ");
        String pattern = scanner.nextLine();
        view.displayMails(repository.wildcardSearch(pattern));
    }
}
