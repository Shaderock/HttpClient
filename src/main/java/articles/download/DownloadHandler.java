package articles.download;

import com.gargoylesoftware.htmlunit.html.HtmlPage;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class DownloadHandler
{
    private int pagesDownloaded;
    private final int pagesToDownload;
    private final Semaphore maxDownloadersSemaphore;
    private final ArrayList<HtmlPage> htmlPages;

    public DownloadHandler(int pagesToDownload)
    {
        pagesDownloaded = 0;
        this.pagesToDownload = pagesToDownload;
        maxDownloadersSemaphore = new Semaphore(4);
        htmlPages = new ArrayList<HtmlPage>();
    }

    public ArrayList<HtmlPage> downloadPages()
    {
        ArrayList<PageDownloader> downloaders = new ArrayList<PageDownloader>();
        int amountOfDownloaders = 6;
        for (int i = 0; i < amountOfDownloaders; i++)
        {
            downloaders.add(new PageDownloader(maxDownloadersSemaphore, this));
            downloaders.get(i).start();
            try
            {
                downloaders.get(i).join();
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
        return htmlPages;
    }

    public synchronized String getNextPageUrlToDownload()
    {
        String result = "https://www.old-games.ru/forum/forums/opoznanie-igr.52/";
        pagesDownloaded++;
        if (pagesDownloaded != 0)
        {
            result += "page-" + pagesDownloaded;
        }
        return result;
    }

    public synchronized boolean continueDownloading()
    {
        return (pagesDownloaded - pagesToDownload) != 0;
    }

    public synchronized void addHtmlPage(HtmlPage htmlPage)
    {
        htmlPages.add(htmlPage);
    }
}
