import articles.Article;
import articles.ArticlesHandler;
import articles.download.DownloadHandler;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import parser.Parser;

import java.util.ArrayList;
import java.util.Scanner;

public class App
{
    public static void main(String[] args)
    {
        executeParser();
        executeDownload();
    }

    private static void executeParser()
    {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите логин: ");
        String login = scanner.nextLine();
        System.out.println("Введите пароль: ");
        String pass = scanner.nextLine();

        Parser parser = new Parser();

        parser.loginUser(login, pass);
        parser.sendGetRequest();
        parser.sendPostRequest();
        parser.sendHeadRequest();
        parser.sendOptionsRequest();
    }

    private static void executeDownload()
    {
        DownloadHandler downloadHandler = new DownloadHandler(7);
        ArrayList<HtmlPage> htmlPages = downloadHandler.downloadPages();
        ArticlesHandler articlesHandler = new ArticlesHandler(htmlPages);
        ArrayList<Article> articles = articlesHandler.extractArticles();

        for (Article article : articles)
        {
            System.out.println("Name: " + article.getName());
            System.out.println("Author: " + article.getAuthor());
        }
    }
}
