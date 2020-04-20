package articles;

import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ArticleExtractor extends Thread
{
    private final Semaphore semaphore;
    private final ArticlesHandler articlesHandler;
    private final ArrayList<Article> articles;

    public ArticleExtractor(Semaphore semaphore,
                            ArticlesHandler articlesHandler)
    {
        this.semaphore = semaphore;
        this.articlesHandler = articlesHandler;
        articles = new ArrayList<Article>();
    }

    @Override
    public void run()
    {
        while (articlesHandler.continueExtracting())
        {
            try
            {
                semaphore.acquire();
                extractArticlesFromPage(articlesHandler.getNextPage());
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
            finally
            {
                semaphore.release();
            }
        }
    }

    private void extractArticlesFromPage(HtmlPage htmlPage)
    {
        DomNodeList<DomNode> articleNamesAnchors = htmlPage
                .querySelectorAll(".listBlock.main .PreviewTooltip");
        DomNodeList<DomNode> articleAuthorsAnchors = htmlPage
                .querySelectorAll(".listBlock.main .username");

        for (int i = 0; i < articleNamesAnchors.getLength(); i++)
        {
            Article article = new Article();
            article.setName(articleNamesAnchors.get(i).asText());
            article.setAuthor(articleAuthorsAnchors.get(i).asText());
            articles.add(article);
        }

        extractArticle();
    }

    private void extractArticle()
    {
        final String regex = ".*PC.*";
        for (Article article : articles)
        {
            final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
            final Matcher matcher = pattern.matcher(article.getName());
            if (matcher.find())
            {
                articlesHandler.addArticle(article);
            }
        }
    }
}
