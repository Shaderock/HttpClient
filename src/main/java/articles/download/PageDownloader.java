package articles.download;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import java.io.IOException;
import java.util.concurrent.Semaphore;

public class PageDownloader extends Thread
{
    private final Semaphore semaphore;
    private final DownloadHandler downloadHandler;
    private final WebClient webClient;

    public PageDownloader(Semaphore semaphore,
                          DownloadHandler downloadHandler)
    {
        this.semaphore = semaphore;
        this.downloadHandler = downloadHandler;
        webClient = new WebClient();
        webClient.getOptions().setJavaScriptEnabled(false);
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setRedirectEnabled(true);
        webClient.getCookieManager().setCookiesEnabled(true);
    }

    @Override
    public void run()
    {
        while (downloadHandler.continueDownloading())
        {
            try
            {
                semaphore.acquire();
                downloadPage(downloadHandler.getNextPageUrlToDownload());
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

    private void downloadPage(String url)
    {
        try
        {
            HtmlPage downloadedPage = webClient.getPage(url);
            downloadHandler.addHtmlPage(downloadedPage);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
