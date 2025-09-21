import java.io.*;
import java.net.*;
import java.text.DecimalFormat;
import com.sun.net.httpserver.*;

public class Main {
    public static void main(String[] args) throws Exception {
        // Tworzymy serwer HTTP na porcie 8080
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        
        // Dodajemy obsługę dla różnych ścieżek
        server.createContext("/", new MainHandler());
        server.createContext("/calculate", new CalculateHandler());
        
        server.setExecutor(null);
        server.start();
        
        System.out.println("Serwer uruchomiony na http://localhost:8080");
        System.out.println("Aby zatrzymać serwer, naciśnij Ctrl+C");
    }
}

// Obsługa strony głównej
class MainHandler implements HttpHandler {
    public void handle(HttpExchange exchange) throws IOException {
        String response = getMainPageHTML();
        
        exchange.getResponseHeaders().set("Content-Type", "text/html; charset=utf-8");
        exchange.sendResponseHeaders(200, response.getBytes("utf-8").length);
        
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes("utf-8"));
        os.close();
    }
    
    private String getMainPageHTML() {
        return "<!DOCTYPE html>" +
               "<html lang='pl'>" +
               "<head>" +
               "<meta charset='UTF-8'>" +
               "<meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
               "<title>Kalkulator BMI</title>" +
               "<style>" +
               "body { font-family: Arial, sans-serif; max-width: 800px; margin: 50px auto; padding: 20px; background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); min-height: 100vh; }" +
               ".container { background: white; padding: 40px; border-radius: 20px; box-shadow: 0 10px 30px rgba(0,0,0,0.2); }" +
               "h1 { color: #333; text-align: center; margin-bottom: 30px; font-size: 2.5em; }" +
               ".form-group { margin-bottom: 25px; }" +
               "label { display: block; margin-bottom: 10px; font-weight: bold; color: #555; font-size: 1.1em; }" +
               "input[type='number'] { width: 100%; padding: 15px; border: 2px solid #ddd; border-radius: 10px; font-size: 16px; transition: border-color 0.3s; }" +
               "input[type='number']:focus { border-color: #667eea; outline: none; box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1); }" +
               "button { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; padding: 15px 40px; border: none; border-radius: 10px; cursor: pointer; font-size: 18px; font-weight: bold; width: 100%; transition: transform 0.2s; }" +
               "button:hover { transform: translateY(-2px); box-shadow: 0 5px 15px rgba(102, 126, 234, 0.4); }" +
               ".info { background: linear-gradient(135deg, #e3f2fd 0%, #bbdefb 100%); padding: 20px; border-radius: 10px; margin-bottom: 25px; border-left: 5px solid #2196F3; }" +
               ".unit-selector { display: flex; gap: 20px; margin-bottom: 15px; }" +
               ".unit-option { display: flex; align-items: center; gap: 8px; }" +
               "input[type='radio'] { transform: scale(1.2); }" +
               ".tip { background: linear-gradient(135deg, #fff3e0 0%, #ffe0b2 100%); padding: 15px; border-radius: 8px; margin-top: 20px; border-left: 4px solid #ff9800; font-size: 0.9em; }" +
               "</style>" +
               "</head>" +
               "<body>" +
               "<div class='container'>" +
               "<h1>💪 Kalkulator BMI</h1>" +
               "<div class='info'>" +
               "<strong>ℹ️ Co to BMI?</strong><br>" +
               "BMI (Body Mass Index) to wskaźnik masy ciała, który pomaga ocenić, czy Twoja waga jest odpowiednia dla Twojego wzrostu. To prosty sposób na sprawdzenie, czy jesteś w zdrowym zakresie wagowym." +
               "</div>" +
               "<form action='/calculate' method='POST'>" +
               "<div class='form-group'>" +
               "<label>📏 Jednostki:</label>" +
               "<div class='unit-selector'>" +
               "<div class='unit-option'>" +
               "<input type='radio' id='metric' name='units' value='metric' checked>" +
               "<label for='metric'>Metryczne (kg, cm)</label>" +
               "</div>" +
               "<div class='unit-option'>" +
               "<input type='radio' id='imperial' name='units' value='imperial'>" +
               "<label for='imperial'>Angielskie (lbs, ft/in)</label>" +
               "</div>" +
               "</div>" +
               "</div>" +
               "<div class='form-group'>" +
               "<label for='weight'>⚖️ Waga:</label>" +
               "<input type='number' id='weight' name='weight' min='1' max='1000' step='0.1' placeholder='np. 70 kg lub 154 lbs' required>" +
               "</div>" +
               "<div class='form-group'>" +
               "<label for='height'>📐 Wzrost:</label>" +
               "<input type='number' id='height' name='height' min='1' max='300' step='0.1' placeholder='np. 175 cm lub 5.9 ft' required>" +
               "</div>" +
               "<div class='form-group'>" +
               "<label for='age'>🎂 Wiek (opcjonalnie):</label>" +
               "<input type='number' id='age' name='age' min='1' max='120' step='1' placeholder='np. 25 lat'>" +
               "</div>" +
               "<button type='submit'>🧮 Oblicz BMI</button>" +
               "<div class='tip'>" +
               "<strong>💡 Wskazówka:</strong> BMI to orientacyjny wskaźnik. Nie uwzględnia masy mięśniowej, struktury kości czy rozkładu tkanki tłuszczowej. Dla dokładnej oceny skonsultuj się z lekarzem lub dietetykiem." +
               "</div>" +
               "</form>" +
               "</div>" +
               "</body>" +
               "</html>";
    }
}

// Obsługa kalkulacji
class CalculateHandler implements HttpHandler {
    public void handle(HttpExchange exchange) throws IOException {
        if ("POST".equals(exchange.getRequestMethod())) {
            // Odczytujemy dane z formularza
            InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(), "utf-8");
            BufferedReader br = new BufferedReader(isr);
            String formData = br.readLine();
            
            // Parsujemy dane
            String[] params = formData.split("&");
            double weight = 0, height = 0;
            int age = 0;
            String units = "metric";
            
            for (String param : params) {
                String[] keyValue = param.split("=");
                if (keyValue.length == 2) {
                    String key = URLDecoder.decode(keyValue[0], "UTF-8");
                    String value = URLDecoder.decode(keyValue[1], "UTF-8");
                    
                    switch (key) {
                        case "weight":
                            weight = Double.parseDouble(value);
                            break;
                        case "height":
                            height = Double.parseDouble(value);
                            break;
                        case "age":
                            if (!value.isEmpty()) {
                                age = Integer.parseInt(value);
                            }
                            break;
                        case "units":
                            units = value;
                            break;
                    }
                }
            }
            
            // Obliczamy BMI
            BMICalculator calculator = new BMICalculator();
            BMIResult result = calculator.calculate(weight, height, age, units);
            
            // Generujemy odpowiedź
            String response = getResultPageHTML(weight, height, age, units, result);
            
            exchange.getResponseHeaders().set("Content-Type", "text/html; charset=utf-8");
            exchange.sendResponseHeaders(200, response.getBytes("utf-8").length);
            
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes("utf-8"));
            os.close();
        }
    }
    
    private String getResultPageHTML(double weight, double height, int age, String units, BMIResult result) {
        DecimalFormat df = new DecimalFormat("#0.0");
        String weightUnit = units.equals("metric") ? "kg" : "lbs";
        String heightUnit = units.equals("metric") ? "cm" : "ft";
        
        String categoryColor = getBMIColor(result.category);
        String categoryEmoji = getBMIEmoji(result.category);
        
        return "<!DOCTYPE html>" +
               "<html lang='pl'>" +
               "<head>" +
               "<meta charset='UTF-8'>" +
               "<meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
               "<title>Wyniki BMI - Kalkulator BMI</title>" +
               "<style>" +
               "body { font-family: Arial, sans-serif; max-width: 900px; margin: 30px auto; padding: 20px; background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); min-height: 100vh; }" +
               ".container { background: white; padding: 40px; border-radius: 20px; box-shadow: 0 10px 30px rgba(0,0,0,0.2); }" +
               "h1 { color: #333; text-align: center; margin-bottom: 30px; font-size: 2.5em; }" +
               ".result-main { background: " + categoryColor + "; color: white; padding: 30px; border-radius: 15px; margin: 25px 0; text-align: center; }" +
               ".bmi-value { font-size: 3.5em; font-weight: bold; margin: 15px 0; }" +
               ".category { font-size: 1.8em; margin: 10px 0; }" +
               ".details { background-color: #f8f9fa; padding: 25px; border-radius: 12px; margin: 25px 0; }" +
               ".detail-row { display: flex; justify-content: space-between; margin: 12px 0; padding: 10px 0; border-bottom: 1px solid #eee; }" +
               ".detail-label { font-weight: bold; color: #555; }" +
               ".detail-value { color: #333; font-weight: 500; }" +
               ".bmi-scale { background: white; padding: 20px; border-radius: 10px; margin: 20px 0; border: 2px solid #eee; }" +
               ".scale-item { display: flex; justify-content: space-between; align-items: center; padding: 12px; margin: 8px 0; border-radius: 8px; font-weight: bold; }" +
               ".scale-underweight { background: #e3f2fd; color: #1976d2; }" +
               ".scale-normal { background: #e8f5e8; color: #388e3c; }" +
               ".scale-overweight { background: #fff3e0; color: #f57c00; }" +
               ".scale-obese { background: #ffebee; color: #d32f2f; }" +
               ".recommendations { background: linear-gradient(135deg, #f3e5f5 0%, #e1bee7 100%); padding: 20px; border-radius: 10px; margin: 20px 0; }" +
               ".back-button { background: linear-gradient(135deg, #6c757d 0%, #495057 100%); color: white; padding: 12px 30px; border: none; border-radius: 8px; cursor: pointer; font-size: 16px; text-decoration: none; display: inline-block; margin-top: 20px; }" +
               ".back-button:hover { transform: translateY(-2px); }" +
               ".warning { background: #fff3cd; color: #856404; padding: 20px; border-radius: 8px; margin: 20px 0; border-left: 4px solid #ffc107; }" +
               "</style>" +
               "</head>" +
               "<body>" +
               "<div class='container'>" +
               "<h1>📊 Wyniki Kalkulatora BMI</h1>" +
               "<div class='result-main'>" +
               "<h2>" + categoryEmoji + " Twoje BMI</h2>" +
               "<div class='bmi-value'>" + df.format(result.bmi) + "</div>" +
               "<div class='category'>" + result.category + "</div>" +
               "</div>" +
               "<div class='details'>" +
               "<h3>📋 Szczegóły obliczeń</h3>" +
               "<div class='detail-row'>" +
               "<span class='detail-label'>⚖️ Waga:</span>" +
               "<span class='detail-value'>" + df.format(weight) + " " + weightUnit + "</span>" +
               "</div>" +
               "<div class='detail-row'>" +
               "<span class='detail-label'>📐 Wzrost:</span>" +
               "<span class='detail-value'>" + df.format(height) + " " + heightUnit + "</span>" +
               "</div>" +
               (age > 0 ? 
               "<div class='detail-row'>" +
               "<span class='detail-label'>🎂 Wiek:</span>" +
               "<span class='detail-value'>" + age + " lat</span>" +
               "</div>" : "") +
               "<div class='detail-row'>" +
               "<span class='detail-label'>🧮 BMI:</span>" +
               "<span class='detail-value'>" + df.format(result.bmi) + " kg/m²</span>" +
               "</div>" +
               "</div>" +
               "<div class='bmi-scale'>" +
               "<h3>📏 Skala BMI</h3>" +
               "<div class='scale-item scale-underweight'>" +
               "<span>🔵 Niedowaga</span><span>&lt; 18.5</span>" +
               "</div>" +
               "<div class='scale-item scale-normal'>" +
               "<span>🟢 Waga prawidłowa</span><span>18.5 - 24.9</span>" +
               "</div>" +
               "<div class='scale-item scale-overweight'>" +
               "<span>🟡 Nadwaga</span><span>25.0 - 29.9</span>" +
               "</div>" +
               "<div class='scale-item scale-obese'>" +
               "<span>🔴 Otyłość</span><span>≥ 30.0</span>" +
               "</div>" +
               "</div>" +
               "<div class='recommendations'>" +
               "<h3>💡 Rekomendacje</h3>" +
               "<p>" + getRecommendations(result.category) + "</p>" +
               "</div>" +
               "<div class='warning'>" +
               "<strong>⚠️ Ważne:</strong> BMI to wskaźnik orientacyjny. Nie uwzględnia budowy ciała, masy mięśniowej ani rozkładu tkanki tłuszczowej. Dla dokładnej oceny stanu zdrowia skonsultuj się z lekarzem lub dietetykiem." +
               "</div>" +
               "<a href='/' class='back-button'>← Oblicz ponownie</a>" +
               "</div>" +
               "</body>" +
               "</html>";
    }
    
    private String getBMIColor(String category) {
        switch (category) {
            case "Niedowaga": return "linear-gradient(135deg, #42a5f5 0%, #1976d2 100%)";
            case "Waga prawidłowa": return "linear-gradient(135deg, #66bb6a 0%, #388e3c 100%)";
            case "Nadwaga": return "linear-gradient(135deg, #ffb74d 0%, #f57c00 100%)";
            case "Otyłość": return "linear-gradient(135deg, #ef5350 0%, #d32f2f 100%)";
            default: return "linear-gradient(135deg, #757575 0%, #424242 100%)";
        }
    }
    
    private String getBMIEmoji(String category) {
        switch (category) {
            case "Niedowaga": return "🔵";
            case "Waga prawidłowa": return "🟢";
            case "Nadwaga": return "🟡";
            case "Otyłość": return "🔴";
            default: return "⚪";
        }
    }
    
    private String getRecommendations(String category) {
        switch (category) {
            case "Niedowaga":
                return "🍎 Rozważ zwiększenie spożycia zdrowych kalorii i skonsultuj się z dietetykiem. " +
                       "Regularne posiłki, ćwiczenia siłowe i konsultacja lekarska mogą pomóc w osiągnięciu zdrowej wagi.";
            case "Waga prawidłowa":
                return "🎉 Gratulacje! Twoja waga jest w zdrowym zakresie. " +
                       "Kontynuuj zrównoważoną dietę i regularną aktywność fizyczną, aby utrzymać obecny stan.";
            case "Nadwaga":
                return "🏃‍♂️ Rozważ wprowadzenie regularnej aktywności fizycznej i zrównoważonej diety. " +
                       "Konsultacja z dietetykiem może pomóc w bezpiecznym osiągnięciu zdrowej wagi.";
            case "Otyłość":
                return "🏥 Zaleca się konsultację z lekarzem lub specjalistą od żywienia. " +
                       "Profesjonalny plan diety i aktywności fizycznej może znacząco poprawić Twoje zdrowie.";
            default:
                return "Skonsultuj się z profesjonalistą zdrowia dla indywidualnych rekomendacji.";
        }
    }
}

// Klasa do obliczeń BMI
class BMICalculator {
    public BMIResult calculate(double weight, double height, int age, String units) {
        double bmiWeight = weight;
        double bmiHeight = height;
        
        // Konwersja jednostek na metryczne
        if ("imperial".equals(units)) {
            bmiWeight = weight * 0.453592; // lbs to kg
            bmiHeight = height * 30.48; // ft to cm
        }
        
        // Konwersja wzrostu na metry
        bmiHeight = bmiHeight / 100;
        
        // Obliczenie BMI
        double bmi = bmiWeight / (bmiHeight * bmiHeight);
        
        // Określenie kategorii
        String category = getBMICategory(bmi);
        
        return new BMIResult(bmi, category);
    }
    
    private String getBMICategory(double bmi) {
        if (bmi < 18.5) {
            return "Niedowaga";
        } else if (bmi < 25.0) {
            return "Waga prawidłowa";
        } else if (bmi < 30.0) {
            return "Nadwaga";
        } else {
            return "Otyłość";
        }
    }
}

// Klasa przechowująca wyniki obliczeń
class BMIResult {
    public final double bmi;
    public final String category;
    
    public BMIResult(double bmi, String category) {
        this.bmi = bmi;
        this.category = category;
    }
}