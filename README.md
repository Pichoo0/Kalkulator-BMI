Dokumentacja Projektu - Kalkulator BMI 

 Opis Założeń 

Cel Aplikacji 

Aplikacja internetowa "Kalkulator BMI" została stworzona w celu umożliwienia użytkownikom szybkiego obliczania wskaźnika masy ciała (Body Mass Index). Program implementuje funkcjonalny formularz przetwarzający wprowadzone dane oraz dostarcza wyniki szczegółowej analizy. 

Funkcjonalności 

Formularz kalkulacyjny - główny element aplikacji umożliwiający wprowadzenie:  

Wagi (w kilogramach lub funtach) 

Wzrostu (w centymetrach lub stopach) 

Wieku (opcjonalnie) 

Wybór systemu jednostek (metryczny/angielski) 

Przetwarzanie danych - aplikacja automatycznie oblicza:  

Wartość BMI 

Kategorię wagową (niedowaga, prawidłowa, nadwaga, otyłość) 

Personalizowane rekomendacje zdrowotne 

Edukacyjny interfejs - zawiera skalę BMI i szczegółowe wyjaśnienia 

Responsywny design - atrakcyjny, kolorowy interfejs z gradientami 

Walidacja danych - sprawdzanie poprawności wprowadzonych wartości 
 
Obraz 
 

Technologie Użyte 

Java - główny język programowania (HttpServer) 

HTML/CSS - frontend z nowoczesnym designem 

Wzorce matematyczne - implementacja standardowego wzoru BMI 

Responsive Web Design - optymalizacja dla różnych urządzeń 

 Prezentacja Działania Programu 

Ekran Główny 

Po uruchomieniu aplikacji użytkownik widzi: 

Nagłówek z tytułem "💪 Kalkulator BMI" 

Panel informacyjny wyjaśniający czym jest BMI 

Formularz z polami:  

Wybór systemu jednostek (radio buttons) 

Waga (z emoji ⚖️) 

Wzrost (z emoji 📐) 

Wiek - opcjonalnie (z emoji 🎂) 

Przycisk "🧮 Oblicz BMI" do wysłania formularza 

Wskazówki o ograniczeniach wskaźnika BMI 
 
Obraz 

Proces Kalkulacji 

Użytkownik wypełnia formularz wybierając jednostki i wprowadzając dane 

System automatycznie konwertuje jednostki angielskie na metryczne jeśli potrzeba 

Aplikacja oblicza BMI według wzoru: BMI = waga(kg) / wzrost(m)² 

System określa kategorię wagową na podstawie standardowych zakresów WHO 

Wyniki są prezentowane na dedykowanej stronie z pełną analizą 
 
Obraz 

Strona Wyników 

Zawiera: 

Główny wynik - wartość BMI w dużej, kolorowej ramce 

Kategoria wagowa - z odpowiednim kolorem i emoji 

Szczegóły obliczeń - tabela z wprowadzonymi danymi 

Skala BMI - wizualizacja wszystkich kategorii wagowych 

Personalizowane rekomendacje - porady zdrowotne dla danej kategorii 

Ostrzeżenie medyczne - o ograniczeniach wskaźnika BMI 

Przycisk powrotu do strony głównej 
Obraz 

System Kategorii BMI (WHO) 

🔵 Niedowaga: BMI < 18.5 

🟢 Waga prawidłowa: BMI 18.5 - 24.9 

🟡 Nadwaga: BMI 25.0 - 29.9 

🔴 Otyłość: BMI ≥ 30.0 

Wzór Matematyczny 

Aplikacja używa standardowego wzoru BMI Światowej Organizacji Zdrowia: 

BMI = masa ciała (kg) / [wzrost (m)]² 
 
Konwersje jednostek: 
- 1 funt (lbs) = 0.453592 kg 
- 1 stopa (ft) = 30.48 cm 
  

 Przykładowe Dane Testowe 

Osoba dorosła: 70 kg, 175 cm → BMI: 22.9 (Waga prawidłowa) 
Obraz 

Test nadwagi: 85 kg, 170 cm → BMI: 29.4 (Nadwaga) 
Obraz 

Test jednostek angielskich: 154 lbs, 5.7 ft → BMI: 23.1 (Waga prawidłowa) 
Obraz 

 Kod Źródłowy 

Link do repozytorium Git: https://github.com/Pichoo0/Kalkulator-BMI.git 

 

 
  

 
