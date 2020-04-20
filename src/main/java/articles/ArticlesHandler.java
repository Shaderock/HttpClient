package articles;

import com.gargoylesoftware.htmlunit.html.HtmlPage;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class ArticlesHandler
{
    private final ArrayList<HtmlPage> htmlPages;
    private final ArrayList<Article> articles;
    private final Semaphore maxExtractorsSemaphore;
    private int nextPage;

    public ArticlesHandler(ArrayList<HtmlPage> htmlPages)
    {
        this.htmlPages = htmlPages;
        articles = new ArrayList<Article>();
        nextPage = 0;
        maxExtractorsSemaphore = new Semaphore(5);
    }

    public ArrayList<Article> extractArticles()
    {
        ArrayList<ArticleExtractor> extractors = new ArrayList<ArticleExtractor>();
        int amountOfExtractors = 6;
        for (int i = 0; i < amountOfExtractors; i++)
        {
            extractors.add(new ArticleExtractor(maxExtractorsSemaphore, this));
            extractors.get(i).start();
            try
            {
                extractors.get(i).join();
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
        return articles;
    }

    public synchronized HtmlPage getNextPage()
    {
        return htmlPages.get(nextPage++);
    }

    public synchronized boolean continueExtracting()
    {
        return nextPage != htmlPages.size();
    }

    public synchronized void addArticle(Article article)
    {
        articles.add(article);
    }
}
