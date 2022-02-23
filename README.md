# ArduCam
Sistema di sorveglianza che utilizza Arduino Cloud, Smartphone iOS/Android e Python 3.
# Utilizzo delle implementazioni di default
Le implementazioni di default sono situate in /client_android e /template_api/python3/flask_mp5/.<br>
Per prima cosa, installa Python 3, se non lo hai già fatto. Una versione recente è consigliata.<br>
Dopodichè, scarica le dipendenze necessarie usando il comando: <br>
<img style="height: 300px" src="https://i.imgur.com/YhRV283_d.webp?maxwidth=760&fidelity=grand"><br>
Infine, esegui lo script:<br>
<img style="height: 300px" src="https://i.imgur.com/od8vbuq.png"><br>
Se tutto va bene, dovremmo vedere il seguente output:<br>
<img style="height: 300px" src="https://i.imgur.com/aS76I2S.png"><br>
Siamo pronti a configurare il nostro Arduino. L'implementazione di default prevede l'utilizzo di Arduino Cloud, quindi avremo bisogno di un Arduino compatibile. Io utilizzo un Arduino Nano 33 IoT con il seguente sketch: <br>
<img style="height: 1000px" src="https://i.imgur.com/CVd2DXx.png"><br>
Lo potete trovare [qui](https://create.arduino.cc/editor/matteofo06/fb1ddf04-2aa1-472e-953e-59153b1678e1/preview).<br>
La schematic è la seguente:<br>
<img style="height: 300px" src="https://i.imgur.com/y9TkDgM.png"><br>
Dopo aver assemblato il circuito su una breadboard ed aver programmato il nostro microcontroller, possiamo al client Android.<br>
Apriamo il progetto ArduCam su Android Studio e compiliamo il progetto andando su Build > Build Bundle(s) / APK(s) > Build APK(s). Seguiamo la procedura a schermo.<br>
Quando la compilazione è finita, andiamo a trovare il nostro file APK prodotto e trasferiamolo sullo smartphone. Infine, andiamo ad installarlo col nostro file browser preferito.
Avviamo l'applicazione, inseriamo il nostro IP (con porta 5000, es. 192.168.1.15:5000) e tappiamo il bottone Registra (in alto).<br>
Diamo accesso alla fotocamera e siamo pronti a scattare foto; quando l'Arduino rileverà un oggetto in distanza breve dal sonar, invierà una richiesta HTTP alla nostra API flask che dirà al telefono di scattare una foto. Possiamo vedere la foto più recente andando su (indirizzo_server):5000/preview o su un altro telefono utilizzando il bottone Monitora (in basso).
# Creare un'implementazione personalizzata
todo
# Licenza
Consultare il file LICENSE.
