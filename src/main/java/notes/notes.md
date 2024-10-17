package notes;
# NEGATIVA SCENARION
Problem
Den nuvarande deleteProduct-metoden anropar helt enkelt productRepository.deleteById(id)
utan att kontrollera om produkten finns. I Spring Data MongoDB ger metoden deleteById
inget undantag om enheten inte finns. Detta innebär att ett försök att ta bort en
icke-existerande produkt tyst kommer att misslyckas, vilket kanske inte är det önskade beteendet.

Önskat beteende
När du försöker ta bort en produkt som inte finns, bör tjänsten skicka
ett ProductNotFoundException (ett anpassat undantag) för att informera
den som ringer att raderingen misslyckades eftersom produkten inte existerar.

Att skriva det underkända testet
Vi skapar ett test som försöker ta bort en produkt med ett ID som inte finns i förvaret.
Vi förväntar oss att tjänsten ger en ProductNotFoundException. Eftersom den nuvarande
implementeringen inte hanterar detta scenario bör testet misslyckas, vilket illustrerar
den saknade funktionaliteten.

Steg-för-steg
Definiera det anpassade undantaget: Skapa en ProductNotFoundException.
Skriv testet: Implementera ett test som förväntar sig detta undantag när
du tar bort en icke-existerande produkt. 
Definiera det anpassade undantaget 
Först skapar vi ett anpassat undantag för att representera scenariot där en produkt inte hittas.

Vi lägger till en ny testmetod i ProductServiceTest.java för att försöka
ta bort en icke-existerande produkt och förväntar oss en ProductNotFoundException.




# Identifierade negativa scenarier

## skapa produkt:
Skapa en produkt där obligatoriska fält saknas (t.ex. namn, pris).
Skapa en produkt med ogiltiga data (t.ex. negativt pris, för stort lagerkvantitet).

## getAllProducts:
Hantera fall där repository returnerar en tom lista.

## getProductsByName:
Hämta produkter med ett namn som inte finns.
Skickar en null eller tom sträng som namn.

## getProductsByPriceRange:
Ogiltiga prisintervall (t.ex. minPrice större än maxPrice).
Negativa prisvärden.

## getProductsByColor:
Hämta produkter med en färg som inte finns.
Skicka en null eller tom sträng som färg.