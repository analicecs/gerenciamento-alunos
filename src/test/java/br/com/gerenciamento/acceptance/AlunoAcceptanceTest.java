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
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class AlunoAcceptanceTest {

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
    void testCadastrarAluno() {
  
        String timestamp = String.valueOf(System.currentTimeMillis());
        String nomeAluno = "Aluno Teste " + timestamp;
        String matriculaAluno = "MAT" + timestamp.substring(timestamp.length() - 6);

        driver.get(BASE_URL + "/inserirAlunos");
        
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        
        wait.until(ExpectedConditions.or(
            ExpectedConditions.presenceOfElementLocated(By.name("nome")),
            ExpectedConditions.urlContains("inserirAlunos")
        ));

        WebElement nomeInput = driver.findElement(By.name("nome"));
        nomeInput.sendKeys(nomeAluno);
        
        try {
            WebElement matriculaInput = driver.findElement(By.name("matricula"));
            matriculaInput.sendKeys(matriculaAluno);
            System.out.println("Matrícula preenchida: " + matriculaAluno);
        } catch (Exception e) {
            System.out.println("Campo matrícula não encontrado...");
        }
        
        try {
            WebElement cursoSelect = driver.findElement(By.name("curso"));
            Select cursoDropdown = new Select(cursoSelect);
            cursoDropdown.selectByValue("ADMINISTRACAO");
            System.out.println("Curso selecionado: ADMINISTRACAO");
        } catch (Exception e) {
            System.out.println("Campo curso não encontrado...");
        }
        
        try {
            WebElement turnoSelect = driver.findElement(By.name("turno"));
            Select turnoDropdown = new Select(turnoSelect);
            turnoDropdown.selectByValue("MATUTINO"); 
            System.out.println("Turno selecionado: MATUTINO");
        } catch (Exception e) {
            System.out.println("Campo turno não encontrado...");
        }
        
        try {
            WebElement statusSelect = driver.findElement(By.name("status"));
            Select statusDropdown = new Select(statusSelect);
            statusDropdown.selectByValue("ATIVO");
            System.out.println("Status selecionado: ATIVO");
        } catch (Exception e) {
            System.out.println("Campo status não encontrado...");
        }

        WebElement salvarButton = driver.findElement(By.cssSelector("button[type='submit']"));
        
        salvarButton.click();

        wait.until(ExpectedConditions.or(
            ExpectedConditions.urlContains("alunos-adicionados"),
            ExpectedConditions.urlContains("listarAlunos"),
            ExpectedConditions.urlContains("index"),
            ExpectedConditions.presenceOfElementLocated(By.className("alert-success")),
            ExpectedConditions.not(ExpectedConditions.urlContains("inserirAlunos"))
        ));

        String currentUrl = driver.getCurrentUrl();
        boolean cadastroSucesso = currentUrl.contains("alunos-adicionados") || 
                                 currentUrl.contains("listarAlunos") ||
                                 currentUrl.contains("index") ||
                                 !driver.findElements(By.className("alert-success")).isEmpty() ||
                                 !currentUrl.contains("inserirAlunos");
        
        assertTrue(cadastroSucesso, 
            "Cadastro de aluno não foi concluído com sucesso. URL atual: " + currentUrl);

        System.out.println("Aluno cadastrado com sucesso: " + nomeAluno);
    }
}