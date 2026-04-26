# 🏦 Sistem de Gestiune Bancară (Bank App)

## 1. Definirea Sistemului

### 1.1 — Acțiuni și interogări posibile în sistem
Aplicația oferă fluxuri complete pentru 3 tipuri de utilizatori (Client, Angajat, Manager), permițând executarea următoarelor operațiuni:

1. **Înregistrare și Autentificare:** Crearea unui cont de client și autentificarea securizată în sistem pe baza de email și parolă.
2. **Deschidere/Închidere Conturi:** Deschiderea de conturi bancare (Curente sau de Economii) cu generare automată de IBAN, respectiv închiderea acestora.
3. **Operațiuni la bancomat (ATM):** Efectuarea de depuneri și retrageri de numerar din conturile selectate.
4. **Transferuri Bancare:** Transferul de fonduri între două conturi, implicând conversie valutară automată (ex: RON -> EUR) în funcție de moneda conturilor.
5. **Generare Extras de Cont:** Afișarea istoricului de tranzacții pentru un anumit cont pe o perioadă de timp definită.
6. **Calcul Avere Consolidată:** Evaluarea totală a portofoliului unui client (însumând soldurile tuturor conturilor) și conversia averii totale într-o monedă de referință.
7. **Emitere și Blocare Carduri:** Generarea de carduri bancare (Curent) și blocarea de urgență a tuturor instrumentelor asociate unui cont.
8. **Cautarea Inteligentă a Clientului:** Căutarea dosarului unui client pe baza email-ului de către un angajat, utilizând *Distanța Levenshtein* pentru a tolera erori de tastare.
9. **Procesare Final de Lună (Batch):** Rularea proceselor lunare globale (aplicarea dobânzilor și reținerea comisioanelor de administrare).
10. **Generare Rapoarte Management:** Extragerea listei cu topul conturilor eligibile (ce depășesc un prag financiar) pentru oferte speciale.
11. **Gestiunea Personalului:** Promovarea unui angajat la gradul de manager.
12. **Ștergere în Cascadă a utilizatorului si conturilor sale:** Eliminarea unui utilizator din sistem, operațiune care declanșează automat ștergerea tuturor conturilor bancare asociate acestuia.

### 1.2 — Tipuri de obiecte din domeniu
1. `User` *(Clasă abstractă)* - Definește atributele generale ale oricărei persoane înregistrate.
2. `Client` - Persoană fizică cu acces la produse financiare.
3. `Angajat` - Personal operațional (ghișeu).
4. `Manager` - Personal de conducere cu acces la rapoarte și gestiunea altor utilizatori.
5. `ContBancar` *(Clasă abstractă)* - Entitatea financiară centrală (reține fonduri, IBAN, monedă).
6. `ContCurent` - Cont pentru operațiuni zilnice, permite atașarea cardurilor și are comision lunar.
7. `ContEconomii` - Cont destinat economisirii, cu dobândă procentuală anuală.
8. `Card` - Instrument de plată cu număr, CVV, PIN și status de securitate.
9. `Tranzactie` *(Clasă imutabilă)* - Reprezentarea oficială (read-only) a unei mișcări de fonduri.
10. `Moneda` / `TipTranzactie` *(Enum-uri)* - Structuri pentru tipizare (RON, EUR, USD, respectiv TRANSFER, DEPUNERE, RETRAGERE, STORNARE).
