package parser;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.NameValuePair;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class Parser
{
    private final WebClient webClient;

    public Parser()
    {
        this.webClient = new WebClient();
        webClient.getOptions().setJavaScriptEnabled(false);
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setRedirectEnabled(true);
        webClient.getCookieManager().setCookiesEnabled(true);

//        ProxyConfig proxyConfig = new ProxyConfig("myhost", myport);
//        webClient.getOptions().setProxyConfig(proxyConfig);
    }

    public void loginUser(String login, String password)
    {
        String loginUrl = "https://www.old-games.ru/forum/";
        String formXPath = "//*[@id=\"login\"]";
        String loginInputName = "login";
        String passInputName = "password";
        try
        {
            HtmlPage page = webClient.getPage(loginUrl);
            HtmlForm form = page.getFirstByXPath(formXPath);
            final HtmlTextInput loginInput = form.getInputByName(loginInputName);
            final HtmlPasswordInput passInput = form.getInputByName(passInputName);
            loginInput.setValueAttribute(login);
            passInput.setValueAttribute(password);
            form.getInputByValue("Вход").click();
            System.out.println("User signed in");
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void sendGetRequest()
    {
        try
        {
            HtmlPage htmlPage = webClient.getPage("https://www.old-games.ru/forum/members/perfect_user.249419/");
            System.out.println(htmlPage.asXml());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        System.out.println("\n================END OF GET REQUEST==============");
    }

    public void sendPostRequest()
    {
        try
        {
            URL url = new URL("https://www.old-games.ru/forum/members/perfect_user.249419/post");
            WebRequest requestSettings = new WebRequest(url, HttpMethod.POST);
            requestSettings.setRequestBody("message=my+new+status&_xfToken=249419%2C1587371042%2C0746b84cfe6ad6366ab52a371b7662b51f5ecbd4&_xfRequestUri=%2Fforum%2Fmembers%2Fperfect_user.249419%2F&_xfNoRedirect=1&_xfToken=249419%2C1587371042%2C0746b84cfe6ad6366ab52a371b7662b51f5ecbd4&_xfResponseType=json");
            webClient.getPage(requestSettings);
            System.out.println("status updated");
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        System.out.println("\n================END OF POST REQUEST==============");
    }

    public void sendHeadRequest()
    {
        try
        {
            URL url = new URL("https://www.old-games.ru");
            WebRequest requestSettings = new WebRequest(url, HttpMethod.HEAD);
            HtmlPage page = webClient.getPage(requestSettings);
            List<NameValuePair> response = page.getWebResponse().getResponseHeaders();
            for (NameValuePair header : response)
            {
                System.out.println(header.getName() + " = " + header.getValue());
            }
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        System.out.println("\n================END OF HEAD REQUEST==============");
    }

    public void sendOptionsRequest()
    {
        try
        {
            URL url = new URL("https://www.old-games.ru");
            WebRequest requestSettings = new WebRequest(url, HttpMethod.OPTIONS);
            HtmlPage page = webClient.getPage(requestSettings);
            System.out.println(page.asXml());
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        System.out.println("\n================END OF OPTIONS REQUEST==============");
    }
}
