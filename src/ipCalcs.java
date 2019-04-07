/**
 *
 * @author Costantino Marco
 * @version 0.01
 */
public class ipCalcs 
{  
    private String firstOctet, secondOctet, thirdOctet, fourthOctet, netBits="", ipAdress;
    private int firstOctetI, secondOctetI, thirdOctetI, fourthOctetI, netBitsI;
    
    private void toOctets(String adress)
    {   // la funzione ha lo scopo di suddividere un indirizzo in 4 ottetti, dai quali sono appunto formati gli indirizzi.
        int dots=0, dotPos=0, slash=0; // dots mi serve per contare i punti della stringa, dotPos mi server per tenere traccia della posizione dei punti
        // controllo se c'è lo slash che indica alla fine dell'indirizzo il numero di bit della rete
        for(int i=0;i<adress.length();i++)
        {
            if(adress.charAt(i)==0x2F) // 0x2F == /
                slash++;        
        }
        
        for(int i=0;i<adress.length();i++)
        {
             if(adress.charAt(i)=='.'||i==adress.length()-1) // scorro l'indirizzo, se trovo un punto oppure se arrivo alla fine dell'indirizzo inizio i controlli
             {
                 if(dots == 0 && adress.substring(dotPos, i).isEmpty()==false)  // se il numero di punti e' zero e la stringa tra il punto trovato e la posizione del punto precedente 
                     firstOctet = adress.substring(dotPos, i);                  // (che all'inizio corrisponde a zero e quindi al primo elemento dell'indirizzo) non è vuota salvo la stringa ( che corrisponde al primo ottetto) nella relativa variabile
                 if(dots == 1 && adress.substring(dotPos, i).isEmpty()==false)  // controllo, insomma, che tra un punto e l'altro ci sia qualcosa, e se c'è salvo il contenuto nella giusta variabile, scegliendola in base al numero di punti contati
                     secondOctet = adress.substring(dotPos, i);
                 if(dots == 2 && adress.substring(dotPos, i).isEmpty()==false)
                     thirdOctet = adress.substring(dotPos, i);
                 if(dots == 3 && adress.substring(dotPos, i+1).isEmpty() == false && slash == 0)    // se non c'è lo slash l'indirizzo termina senza nulla
                     fourthOctet  = adress.substring(dotPos, i+1);
                 dots++;                                                        // incremento il numero di punti letti
                 dotPos = i+1;                                                  // setto la posizione del punto+1 perchè la funzione substring considera la stringa tra il primo indice compreso ed il secondo non compreso
             }
             if(adress.charAt(i)=='/' && dots==3 && adress.substring(dotPos, i).isEmpty()==false)   // se c'è lo slash l'indirizzo termina con /x 
             {
                 if(dots == 3 && adress.substring(dotPos, i).isEmpty() == false && slash == 1)  // se ho contato 3 punti e c'è lo slash
                     fourthOctet  = adress.substring(dotPos, i);                                // salvo l'ottetto dal punto precedente allo slash
                 netBits = adress.substring(i+1, adress.length());                              // la restante stringa ( tranne lo slash ) viene salvata nella variabile per il numero di bit della rete
             }
        }
        if(slash == 0)                                                          // se non ho trovato lo slash setto a nulla la stringa del numero di bit della rete, perchè riutilizzando la funzione, le variabili devono essere "pulite" per evitare errori
            netBits="";                                                         // nello specifico: utilizzando la funzione con un indirizzo con /x e poi utilizzandola con una senza, se dopo effettuo un controllo sul numero di bit, può risultare errato per colpa del primo indirizzo analizzato
    }
    
    private String toBinary(String toConvert)
    {   // lo scopo di questa funzione è quello di convertire un indirizzo in binario
        toOctets(toConvert);    // suddivido l'indirizzo in ottetti, e quindi carico le variabili  
        firstOctetI = Integer.parseInt(firstOctet); // setto i corrispettivi interi delle stringhe ( converto gli ottetti in interi )
        secondOctetI = Integer.parseInt(secondOctet);
        thirdOctetI = Integer.parseInt(thirdOctet);
        fourthOctetI = Integer.parseInt(fourthOctet);
        String binIp;
        if(netBits.isEmpty()==true) // se netBits è vuota ( questo è un motivo per il quale resettarlo nella funzione toOctets ) compongo l'indirizzo con le conversioni binarie degli interi e con i punti tra un ottetto e l'altro
            binIp = Integer.toBinaryString(firstOctetI)+"."+Integer.toBinaryString(secondOctetI)+"."+Integer.toBinaryString(thirdOctetI)+"."+Integer.toBinaryString(fourthOctetI); 
        else
        {
            netBitsI = Integer.parseInt(netBits);   // se netBits non è vuota faccio lo stesso, aggiungendo però anche il numero di bit della rete preceduti dallo /
            binIp = Integer.toBinaryString(firstOctetI)+"."+Integer.toBinaryString(secondOctetI)+"."+Integer.toBinaryString(thirdOctetI)+"."+Integer.toBinaryString(fourthOctetI)+"/"+Integer.toBinaryString(netBitsI);
        }
        return binIp;
    }    
    
    public ipCalcs(String ipAdress)
    {
        this.ipAdress = ipAdress;
    }
    
    public boolean ipVerification()
    {
        //Il primo controllo sulla stringa riguarda tutto ciò che è diverso da: numeri, punti, slash: se c'è qualcosa di diverso da quelli, l'indirizzo non è valido
        int dots=0, slash = 0;
        for(int i=0;i<ipAdress.length();i++)
        {
            if(ipAdress.charAt(i)<0x2E || ipAdress.charAt(i)>0x39)
            {
                return false;
            }
            if(ipAdress.charAt(i)==0x2E)
                dots++;
            if(ipAdress.charAt(i)==0x2F)
                slash++;        
        }
        if(dots!=3)  // controllo che ci sia un numero coerente di punti ( 3 )
           return false;
        if(slash>1||slash<0) // controllo che ci sia un numero coerente di slash ( 0 o 1 )
            return false;
        // divisione della stringa in ottetti
        dots=0;
        int dotPos=0;
        for(int i=0;i<ipAdress.length();i++)
        {
             if(ipAdress.charAt(i)=='.'||i==ipAdress.length()-1)
             {
                 if(dots == 0 && ipAdress.substring(dotPos, i).isEmpty()==false)
                     firstOctet = ipAdress.substring(dotPos, i);
                 if(dots == 1 && ipAdress.substring(dotPos, i).isEmpty()==false)
                     secondOctet = ipAdress.substring(dotPos, i);
                 if(dots == 2 && ipAdress.substring(dotPos, i).isEmpty()==false)
                     thirdOctet = ipAdress.substring(dotPos, i);
                 if(dots == 3 && ipAdress.substring(dotPos, i+1).isEmpty() == false && slash == 0)
                     fourthOctet  = ipAdress.substring(dotPos, i+1);
                 dots++;
                 dotPos = i+1;                       
             }
             if(ipAdress.charAt(i)=='/' && dots==3 && ipAdress.substring(dotPos, i).isEmpty()==false)
             {
                 if(dots == 3 && ipAdress.substring(dotPos, i).isEmpty() == false && slash == 1)
                     fourthOctet  = ipAdress.substring(dotPos, i);                 
                 netBits = ipAdress.substring(i+1, ipAdress.length());
             }
        }
        // conversione in interi degli ottetti        
        firstOctetI = Integer.parseInt(firstOctet);
        secondOctetI = Integer.parseInt(secondOctet);
        thirdOctetI = Integer.parseInt(thirdOctet);
        fourthOctetI = Integer.parseInt(fourthOctet);
        if(netBits.isEmpty()==false)
            netBitsI = Integer.parseInt(netBits);
        // controllo che i valori degli ottetti siano plausibili
        if(firstOctetI>239||firstOctetI<0)
             return false;
        else if(secondOctetI>255||secondOctetI<0)
             return false;
        else if(thirdOctetI>255||thirdOctetI<0)
             return false;
        else if(fourthOctetI>255||fourthOctetI<0)
             return false;
        else if((netBitsI>30||netBitsI<7)&&netBits.isEmpty()==false)
             return false;
        else 
             return true;
         
    } 
    
    public void setIp(String ipAdress)
    {
        this.ipAdress = ipAdress;
    }
    
    public String getIp()
    {
        return ipAdress;
    }
    
    public String getBinaryIp()
    {
        return toBinary(getIp());
    }
    
    public String getClassIp()
    {
        toOctets(ipAdress);
        int intFirstOctet;
        intFirstOctet = Integer.parseInt(firstOctet);
        String iPclass = "";
                
        if(intFirstOctet<128)   // molto semplicemente controllo il primo ottetto, e in base al suo valore associo la classe all'indirizzo
            iPclass = "A";
        else if(intFirstOctet>=128&&intFirstOctet<192)
            iPclass = "B";
        else if(intFirstOctet>=192&&intFirstOctet<224)
            iPclass = "C";
        else if(intFirstOctet>=224)
            iPclass = "D";
        
        return iPclass;
    }  
    
    public String getBinNetMask()
    {
        toOctets(ipAdress); //divido l'indirizzo in ottetti ( e quindi setto le variabili )
        String netMask = "";
        char[] cAA = new char[35]; // creo un vettore di caratteri, per settare ad uno ad uno i bit nel caso in cui ci sia /x
        if(netBits.isEmpty())   // se non c'è /x controllo come nelle classi, e sulla base dei controlli setto i bit di rete ad 1
        {
            int intFirstOctet;
            intFirstOctet = Integer.parseInt(firstOctet);
            
            if(intFirstOctet<128)
                netMask = "11111111.00000000.00000000.00000000";
            else if(intFirstOctet>=128&&intFirstOctet<192)
                netMask = "11111111.11111111.00000000.00000000";
            else if(intFirstOctet>=192&&intFirstOctet<224)
                netMask = "11111111.11111111.11111111.00000000";
            else if(intFirstOctet>=224)
                netMask = "11111111.11111111.11111111.11111111";
        
        }
        else    // altrimenti eseguo un ciclo, per scrivere la netmask
        {
            int dotC = 0;   // dotC mi serve per contare il numero di bit scritti
            int wOBit = 0;      // wOBit mi server per contare il numero di bit 1 scritti
            for(int i=0;i<35;i++)
            {
                if(wOBit<netBitsI&&dotC<8)  // se il numero di bit 1 scritti è minore del numero di bit 1 da scrivere ( che corrispondono a quelli della rete ) e il numero di bit è minore di 8
                {
                    cAA[i] = '1';   // scrivo 1
                    dotC++;         // e incremento i contatori
                    wOBit++;
                }
                else if(wOBit>=netBitsI&&dotC<8)    // se ho finito di settare ad 1 i bit della rete e il numero di bit scritti è minore di 8 
                {
                    cAA[i] = '0';   // scrivo 0
                    dotC++;         // incremento i contatori
                    wOBit++;
                }
                else if(dotC == 8)  // se il numero di bit scritti corrisponde ad 8
                {
                    cAA[i] = '.';   // scrivo un . perchè ho finito di scrivere l'ottetto e devo passare al successivo
                    dotC = 0;       // resetto il numero di bit dell'ottetto
                }
            }
            String ris = new String(cAA);   // creo una stringa che corrisponda al vettore di caratteri ( unico modo di far equivalere un vettore di caratteri ad una stringa in java )
            netMask = ris;                  
        }
        
        return netMask; 
    }
    
    public String getDecNetMask()
    {
        String decNetMask, binNetMask;
        binNetMask = getBinNetMask();   // mi prendo l'indirizzo binario
        toOctets(binNetMask);   // divido l'indirizzo binario in ottetti
        firstOctetI = Integer.parseInt(firstOctet, 2);  // trasformo gli ottetti in base 10
        secondOctetI = Integer.parseInt(secondOctet, 2);
        thirdOctetI = Integer.parseInt(thirdOctet, 2);
        fourthOctetI = Integer.parseInt(fourthOctet, 2);
        decNetMask = firstOctetI+"."+secondOctetI+"."+thirdOctetI+"."+fourthOctetI; //compongo l'indirizzo con gli ottetti e i punti
        toOctets(ipAdress);
        return decNetMask;
    }
    
    public String getBinWildCard()
    {
        toOctets(ipAdress); 
        String wildCard, netMask;
        netMask = getBinNetMask(); // prendo la netmask in binario
        char[] cAA = new char[35];
        cAA = netMask.toCharArray();    // trasformo la stringa in array di caratteri
        for(int i=0;i<35;i++)
        {
            if(cAA[i]=='1') // se trovo 1 metto 0
            {
                cAA[i] = '0';
            }
            else if(cAA[i]=='0')    // se trovo 0 metto 1 in pratica la wildcard è la negazione della netmask
            {
                cAA[i] = '1';
            }
        }
        String ris = new String(cAA);
        wildCard = ris;
        return wildCard;
    }
    
    public String getDecWildCard()
    {
        String decWildCard, binWildCard;    // faccio la stessa cosa fatta con la netmask
        binWildCard = getBinWildCard();
        toOctets(binWildCard);
        firstOctetI = Integer.parseInt(firstOctet, 2);
        secondOctetI = Integer.parseInt(secondOctet, 2);
        thirdOctetI  = Integer.parseInt(thirdOctet, 2);
        fourthOctetI = Integer.parseInt(fourthOctet, 2);
        decWildCard = firstOctetI+"."+secondOctetI+"."+thirdOctetI+"."+fourthOctetI;
        toOctets(ipAdress);
        return decWildCard;
    }
    
    public String getDecNetwork()
    {
        String decNetworkA="";
        String adress = getIp();
        String netMask = getDecNetMask();
        // metto in delle variabili (intere) tutti gli ottetti, sia dell'indirizzo che della netmask ( decimali )
        int fAOctet, sAOctet, tAOctet, lAOctet;
        int fMOctet, sMOctet, tMOctet, lMOctet;
        int fNOctet, sNOctet, tNOctet, lNOctet;
        
        toOctets(adress); 
        fAOctet = Integer.parseInt(firstOctet);
        sAOctet = Integer.parseInt(secondOctet);
        tAOctet = Integer.parseInt(thirdOctet);
        lAOctet = Integer.parseInt(fourthOctet);
        
        toOctets(netMask);
        fMOctet = Integer.parseInt(firstOctet);
        sMOctet = Integer.parseInt(secondOctet);
        tMOctet = Integer.parseInt(thirdOctet);
        lMOctet = Integer.parseInt(fourthOctet);
        
        // eseguo l'and tra gli ottetti dell'indirizzo e quelli della netmask
        fNOctet = fAOctet & fMOctet;
        sNOctet = sAOctet & sMOctet;
        tNOctet = tAOctet & tMOctet;
        lNOctet = lAOctet & lMOctet;
        decNetworkA = fNOctet+"."+sNOctet+"."+tNOctet+"."+lNOctet; // con il risultato compongo il vettore che restituisco
        return decNetworkA;
    }

    public String getBinNetwork()
    {
        return toBinary(getDecNetwork());
    }
    
    public String getDecBroadcast()
    {
        String decBroadcastA="";    // funziona esattamente con il network, solamente che esegue l'or tra l'indirizzo e la wildcard
        String adress = getIp();
        String netMask = getDecWildCard();
        int fAOctet, sAOctet, tAOctet, lAOctet;
        int fWOctet, sWOctet, tWOctet, lWOctet;
        int fNOctet, sNOctet, tNOctet, lNOctet;
        toOctets(adress);
        fAOctet = Integer.parseInt(firstOctet);
        sAOctet = Integer.parseInt(secondOctet);
        tAOctet = Integer.parseInt(thirdOctet);
        lAOctet = Integer.parseInt(fourthOctet);
        toOctets(netMask);
        fWOctet = Integer.parseInt(firstOctet);
        sWOctet = Integer.parseInt(secondOctet);
        tWOctet = Integer.parseInt(thirdOctet);
        lWOctet = Integer.parseInt(fourthOctet);
        fNOctet = fAOctet | fWOctet;
        sNOctet = sAOctet | sWOctet;
        tNOctet = tAOctet | tWOctet;
        lNOctet = lAOctet | lWOctet;
        decBroadcastA = fNOctet+"."+sNOctet+"."+tNOctet+"."+lNOctet;
        return decBroadcastA;        
    }
    
    public String getBinBroadcast()
    {
        return toBinary(getDecBroadcast());
    }
    
    public String getNHost()
    {
        toOctets(ipAdress);
        double nHost=1;
        int numHost;
        if(netBits.isEmpty()==true) // se non c'è il numero di bit di rete setto i valori standard sulla base delle varie classi
        {
           if(getClassIp()=="A")
               nHost = 16777214;
           if(getClassIp()=="B")
               nHost = 65534;
           if(getClassIp()=="C"||getClassIp()=="D")
               nHost = 254;  
        }   
        else    // altrimenti, elevo 2 alla numero di bit di host ( 3^(32-x) con x che è /x ) e tolgo 2 perchè 2 indirizzi sono riservati uno all'indirizzo di rete e l'altro a quello di broadcast
        {
            netBitsI = Integer.parseInt(netBits);
            nHost = (int) (Math.pow(2, 32-netBitsI)-2);
        }
        numHost = (int) nHost;
        return numHost+"";
    }
    
    public String getNNet()
    {
        toOctets(ipAdress); //funziona allo stesso modo del calcolo del numero di host
        double nNet=1;
        int numNet;
        if(netBits.isEmpty()==true)
        {
           if(getClassIp()=="A")
               nNet = 128;
           if(getClassIp()=="B")
               nNet = 16384;
           if(getClassIp()=="C"||getClassIp()=="D")
               nNet = 2097152;  
        }   
        else    // solo che elevo 2^x essendo x il numero di bit di rete
        {
            netBitsI = Integer.parseInt(netBits);
            nNet = Math.pow(2, netBitsI);
        }
        numNet = (int) nNet;
        return numNet+"";
    }
    
    public String getDecMinHost()
    {
        String network = getDecNetwork();
        toOctets(network);
        fourthOctetI = Integer.parseInt(fourthOctet);   //aggiungo all'ultimo ottetto, dell'indirizzo di network, 1
        fourthOctetI++;
        fourthOctet = Integer.toString(fourthOctetI);
        String minHost = firstOctet+"."+secondOctet+"."+thirdOctet+"."+fourthOctet;
        toOctets(ipAdress);
        return minHost;
    }
    
    public String getBinMinHost()
    {
        String binMinHost = getDecMinHost();
        return toBinary(binMinHost);
    }
    
    public String getDecMaxHost()
    {
        String network = getDecBroadcast();
        toOctets(network);
        fourthOctetI = Integer.parseInt(fourthOctet);   // tolgo 1 all'ultimo ottetto dell'indirizzo di broadcast
        fourthOctetI--;
        fourthOctet = Integer.toString(fourthOctetI);
        String maxHost = firstOctet+"."+secondOctet+"."+thirdOctet+"."+fourthOctet;
        toOctets(ipAdress);
        return maxHost;
    }
    
    public String getBinMaxHost()
    {
        String binMaxHost = getDecMaxHost();
        return toBinary(binMaxHost);
    }
    
    public String getTypeInternet()
    {
        String internet="";
        toOctets(ipAdress);
        firstOctetI = Integer.parseInt(firstOctet);
        secondOctetI = Integer.parseInt(secondOctet);
        if(firstOctetI==10)                             // se gli ottetti sono compresi tra i valori prestabiliti la rete risulta privata
            internet = "(Private Internet)";
        else if(firstOctetI==172&&(secondOctetI>15&&secondOctetI<32))
            internet = "(Private Internet)";
        else if(firstOctetI==192&&secondOctetI==168)
            internet = "(Private Internet)";
        else                                            // in tutti gli altri casi la rete è pubblica
            internet = "(Public Internet)";
        
        return internet;
    }
}
