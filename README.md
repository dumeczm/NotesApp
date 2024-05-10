

MemoStack, aplicatie pentru notite 



Proiect realizat de studentul:  

Cozma Ciprian, grupa 1131 

1. Introducere 
 
MemoStack este o aplicație de gestionare a notițelor concepută pentru a ajuta să organizați eficient gândurile, ideile și sarcinile zilnice. Cu o interfață intuitivă și caracteristici robuste, MemoStack oferă o soluție completă pentru oricine caută să captureze și să acceseze informații rapid și eficient. 

Funcționalități Principale: 

Autentificare securizată: Accesați notițele într-un mod sigur. Fiecare utilizator are propriul său spațiu privat, protejat prin autentificare, asigurând că informațiile dvs. rămân confidențiale și securizate. 

Organizare pe categorii: Structurați notițele pe categorii personalizate pentru a vă îmbunătăți fluxul de lucru și pentru a găsi rapid informațiile necesare. Fie că este vorba de idei pentru proiecte, liste de cumpărături sau gânduri personale, categoriile ajută la menținerea ordinii. 

Editare flexibilă: Modificați și actualizați notițele cu ușurință. Interfața prietenoasă permite ajustări rapide ale conținutului, fie că doriți să adăugați detalii suplimentare, să restructurați sau să corectați erori. 

Ștergere rapidă: Eliminați notițele care nu mai sunt necesare cu doar câteva clicuri, menținând aplicația curată și organizată. 

 

2. Descrierea problemei 

 

În perioada mea de student, lucram part-time într-un mediu agitat, unde trebuia să țin evidența multor sarcini și detalii. De la întâlniri și termene limită până la sarcini rapide pe care colegii sau șefii le menționau pe fugă, era esențial să pot captura aceste informații rapid și eficient. 

Am simțit nevoia unei soluții care să îmi permită să structurez notițele într-un mod mai organizat, să le accesez rapid și să le editez cu ușurință. Așa m-am hotărât să dezvolt NoteKeeper, o aplicație de notițe proiectată să ofere o modalitate intuitivă de a organiza informațiile prin categorii, de a le accesa rapid și de a le actualiza, toate din mers.  

 

3. Descriere API 

 

Firebase Realtime Database 

Firebase Realtime Database este un serviciu de baze de date oferit de platforma Firebase de la Google, conceput pentru a oferi o soluție de stocare și sincronizare a datelor în timp real între clienții utilizatorilor. Este un serviciu de baze de date NoSQL, care stochează datele sub formă de JSON și permite conectarea simultană a milioane de clienți, oferind acces la date în timp real. Acest lucru este deosebit de util pentru aplicații care necesită sincronizare imediată a datelor, cum ar fi aplicațiile de chat, jocurile multiplayer sau orice aplicație care necesită actualizarea conținutului în timp real pentru utilizatori. 

 

Firebase Authentication 

Firebase Authentication are scopul de a facilita gestionarea autentificării și autorizării utilizatorilor în aplicații. Acest serviciu suportă autentificarea prin diverse metode, incluzând parole, numere de telefon, și autentificatori populari precum Google, Facebook și Twitter, printre alții. 

 

4. Flux de date 

Pentru operațiuni CRUD (Create,Read,Update,Delete) am creat clasa NoteManager care ajută la interacțiunile cu Firebase Realtime Database. Această clasă folosește OkHttpClient pentru a face cereri HTTP către Firebase.  

1. Crearea notițelor (createNote) 

Formarea datelor: Se creează un șir JSON care conține ID-ul notiței, titlul, categoria și conținutul. Acest JSON este împachetat într-un RequestBody pentru a fi trimis. 

Cerere HTTP: Se construiește o cerere PUT la URL-ul specific notiței (baseUrl + noteId + ".json"), care înlocuiește orice date existente la acel path sau creează o nouă notiță dacă nu există. 

Procesare Răspuns: Răspunsul de la server este procesat în metoda onResponse. În caz de succes, se loghează succesul; în caz contrar, se loghează eroarea și se apelează callback-ul de eroare. 

 

2. Citirea tuturor notițelor (readAllNotes) 

Cerere HTTP: Se efectuează o cerere GET la URL-ul care conține toate notițele (notesFetcherUrl). 

Procesare răspuns: În onResponse, dacă cererea este reușită, datele (JSON-ul cu toate notițele) sunt extrase și trimise înapoi prin intermediul unui FirebaseCallback pentru a fi procesate în aplicație. 

 

3. Actualizarea Notițelor (updateNote) 

Formarea datelor: Similar cu createNote, se formează un JSON cu datele actualizate ale notiței. 

Cerere HTTP: Se folosește o cerere PATCH la URL-ul notiței pentru a actualiza parțial informațiile. PATCH permite modificarea unor câmpuri specifice fără a înlocui întregul document. 

Procesare răspuns: Se verifică succesul operațiunii și se apelează callback-ul corespunzător. 

 

4. Ștergerea Notițelor (deleteNote) 

Cerere HTTP: Se construiește și se trimite o cerere DELETE la URL-ul specific notiței. 

Procesare răspuns: În funcție de răspuns, se apelează callback-ul de succes sau eroare. 

 

Fluxul General de Date 

Inițializare: La crearea unei instanțe NoteManager, se inițializează clientul HTTP și se setează URL-urile bazei de date. 

Interacțiunea cu Firebase: Fiecare metodă construiește o cerere HTTP adecvată și gestionează răspunsurile. 

Procesarea Callback-urilor: Toate metodele primesc obiecte Callback care sunt apelate cu rezultatele operațiunilor, fie că sunt succes, fie că sunt erori. Acest lucru permite UI-ului să reacționeze la schimbările de stare (de exemplu, actualizând UI-ul după o actualizare reușită). 

Prin folosirea OkHttpClient și structura JSON, NoteManager facilitează o interacțiune eficientă și directă cu Firebase, asigurând că aplicația poate gestiona datele notițelor în mod dinamic și în timp real. Aceasta structură este esențială pentru menținerea unei experiențe utilizator fluidă și responsivă. 

5. Capturi de ecran 

 

		 

 

În imaginile prezentate mai sus se avem modalitatea de Log In și Register în aplicație. 

Autentificarea se face pe baza unui email si a unei parole, care sunt salvate folosind Firebase Authentication. 

Ambele pagini sunt proiectate pentru a fi accesibile și ușor de utilizat, Utilizarea unui design clar și minim fără distragere a atenției asigură că utilizatorii pot naviga și introduce datele necesare rapid și eficient. 

		 

 

Fiecare captură de ecran prezintă notițele organizate sub o categorie specifică. În prima imagine, categoria este Munca, iar în a doua, categoria este Personal. Acest lucru permite utilizatorilor să segmenteze și să acceseze notițele în funcție de contextul lor, facilitând astfel organizarea și căutarea eficientă. 

În fiecare categorie, notițele sunt listate sub forma unor carduri interactive care afișează titlul și un scurt rezumat al conținutului. De exemplu, în categoria Munca, una dintre notițe este intitulată Șterge fișierele cu un subtext care menționează necesitatea ștergerii fișierelor dintr-o locație default.  

 

 

		 

Prima imagine arată fereastra de adăugare a unei noi notițe. Utilizatorii pot selecta o categorie dintr-un meniu drop-down, introduc un titlu și conținutul notiței în câmpurile text corespunzătoare.  

Sub formularul de introducere a datelor se află două butoane: „Anulează”, care permite utilizatorului să închidă fereastra fără a salva notița, și „Adaugă”, care salvează notița în baza de date a aplicației. 

A doua imagine afișează un meniu popup care apare când un utilizator apasă lung pe o notiță existentă în lista principală. Meniul oferă trei opțiuni: Update, pentru a modifica detaliile notiței; Delete, pentru a elimina definitiv notița; și Cancel, pentru a închide meniul fără a efectua o acțiune. 

 

 

 

 

6. Link-uri  

	A. Github: https://github.com/dumeczm/NotesApp.git

	B. Youtube: https://www.youtube.com/watch?v=S0YjIvp11Y0
  

8. Referinte 

Firebase Documentation: https://firebase.google.com/docs/guides 

OkHTTP3 Documentation: https://square.github.io/okhttp/ 

OpenAI: https://openai.com 

 
