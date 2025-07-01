package br.com.gerenciamento.acceptance;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UsuarioAcceptanceTest { 

    private WebDriver driver;
    private final String BASE_URL = "http://localhost:8080";

    @BeforeEach
    void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    void testCadastrarNovoUsuario() {
        driver.get(BASE_URL + "/cadastro");

        String timestamp = String.valueOf(System.currentTimeMillis());
        String emailUsuario = "novo.usuario.selenium." + timestamp + "@example.com";
        String username = "user" + timestamp;
        String senhaUsuario = "NovaSenha@123";

        WebElement emailInput = driver.findElement(By.name("email"));
        WebElement userInput = driver.findElement(By.name("user"));
        WebElement senhaInput = driver.findElement(By.name("senha"));
        WebElement cadastrarButton = driver.findElement(By.cssSelector("button[type='submit']"));

        emailInput.sendKeys(emailUsuario);
        userInput.sendKeys(username);
        senhaInput.sendKeys(senhaUsuario);
        cadastrarButton.click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.urlToBe(BASE_URL + "/")); 

        assertEquals(BASE_URL + "/", driver.getCurrentUrl(), "Não foi redirecionado para a página de login após o cadastro.");

        System.out.println("Teste de Aceitação de Cadastro de Novo Usuário concluído com sucesso para: " + username);
    }
}