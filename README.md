# 🤖 Training Controller – Robot AI

## 🎯 Scopo del progetto

Questo programma serve per creare il dataset di addestramento di un robot autonomo controllato da rete neurale.

Il robot:

- osserva l’ambiente tramite sensori di distanza
- si muove usando due motori/cingoli
- deve imparare a evitare ostacoli e trovare un percorso

La rete neurale NON viene programmata con regole tipo:

```text
se ostacolo → gira
```

ma apprende dai dati raccolti durante una sessione di training umano.

---

# 🧠 Idea principale

Lo studente NON controlla continuamente il robot come una macchina telecomandata.

Invece:

1. il robot effettua uno scan dell’ambiente
2. lo studente sceglie una strategia modificando le velocità dei motori
3. il robot esegue autonomamente quella strategia
4. il sistema registra il comportamento corretto

In questo modo il dataset rappresenta:

```text
situazione iniziale
→
strategia scelta
→
durata della strategia
```

che è esattamente il comportamento che la rete neurale dovrà imparare.

---

# ⚙️ Architettura del sistema

Il sistema è composto da:

```text
PC Java
↓ USB seriale ↑
Arduino / Robot
```

---

## 💻 Applicazione Java

L’applicazione Java:

- legge la tastiera
- mostra le velocità correnti dei motori
- invia i comandi ad Arduino
- riceve gli scan dei sensori
- salva automaticamente il dataset

---

## 🤖 Arduino

Arduino:

- legge i sensori
- muove i motori
- invia gli scan al PC
- esegue le velocità ricevute

---

# 🎮 Controlli

## Modifica velocità motore sinistro

| Tasto | Azione                 |
| ----- | ---------------------- |
| W     | aumenta velocità SX    |
| A     | diminuisce velocità SX |

---

## Modifica velocità motore destro

| Tasto | Azione                 |
| ----- | ---------------------- |
| I     | aumenta velocità DX    |
| L     | diminuisce velocità DX |

---

# 🚀 Start / Stop esecuzione

| Tasto            | Azione                                  |
| ---------------- | --------------------------------------- |
| SPACE            | avvia il robot con le velocità correnti |
| SPACE (di nuovo) | ferma il robot e salva l’esempio        |

---

# ⛔ Fine sessione

| Tasto | Azione                          |
| ----- | ------------------------------- |
| ENTER | termina la sessione di training |

---

# 🔍 Workflow corretto

## 1️⃣ Il robot effettua uno scan

Esempio:

```text
120,180,40,20,10
```

---

## 2️⃣ Lo studente decide la strategia

Modifica gradualmente:

```text
VEL SX
VEL DX
```

osservando i valori nella console Java.

---

## 3️⃣ SPACE → il robot parte

Il robot:

- mantiene autonomamente le velocità
- ignora ulteriori input
- continua fino al prossimo check

---

## 4️⃣ SPACE → stop e salvataggio

Il sistema salva:

```text
scan iniziale,
velocità sx,
velocità dx,
durata
```

---

# 📊 Dataset generato

Il file:

```text
dataset.csv
```

viene creato automaticamente.

Ogni riga rappresenta un esempio di training.

---

## Esempio

```text
120,180,40,20,10,-0.3,1.0,650
```

Traduzione:

```text
scan ambiente
↓
motore SX = -0.3
motore DX = 1.0
↓
mantieni strategia per 650 ms
```

---

# 🧠 Perché questa architettura è importante

La rete neurale finale funzionerà esattamente nello stesso modo:

```text
SCAN
↓
decisione
↓
movimento autonomo
↓
nuovo scan
```

Quindi il dataset è coerente con il comportamento reale del robot.

---

# ⚠️ Suggerimenti per il training

## ✅ Fare

- scegliere strategie coerenti
- lasciare eseguire il robot
- raccogliere esempi diversi

---

## ❌ Evitare

- micro-correzioni continue
- cambiare continuamente strategia
- salvare esempi sbagliati o casuali

---

# 📦 Librerie usate

## jSerialComm

Per la comunicazione seriale Java ↔ Arduino.

Sito ufficiale:

[https://fazecast.github.io/jSerialComm/](https://fazecast.github.io/jSerialComm/)

---

# 🚀 Obiettivo finale

Una volta raccolto il dataset:

1. il CSV verrà usato per addestrare una rete neurale con MathAI
2. i pesi verranno esportati
3. Arduino eseguirà autonomamente il modello neurale

---

# 💡 Idea chiave del progetto

> Il robot non viene programmato con regole esplicite:
> apprende osservando strategie corrette create dagli studenti.

---

# 🧱 Struttura del progetto Java

```text
src/
│
├── TrainingController.java
├── RobotConnection.java
├── MotorState.java
├── TrainingSample.java
└── DatasetRecorder.java
```

---

# 🧠 Significato delle classi

| Classe             | Responsabilità                             |
| ------------------ | ------------------------------------------ |
| TrainingController | gestione tastiera e workflow               |
| RobotConnection    | comunicazione seriale con Arduino          |
| MotorState         | stato delle velocità dei motori            |
| TrainingSample     | rappresentazione di un esempio di training |
| DatasetRecorder    | salvataggio automatico CSV                 |

---

# 🔬 Possibili estensioni future

- più sensori
- più punti di scan
- velocità dinamiche
- più layer neurali
- controllo tramite joystick
- visualizzazione grafica degli scan
- simulazione ambiente
- rete neurale direttamente su Arduino

---

# 🎓 Concetti di informatica coinvolti

Questo progetto unisce:

- programmazione ad oggetti
- comunicazione seriale
- robotica
- reti neurali
- machine learning
- gestione dataset
- sistemi embedded
- controllo motori
- progettazione software
- AI applicata

---

# 🤖 Domanda finale

> Il robot sta davvero “capendo” l’ambiente…
> oppure sta solo applicando una funzione matematica imparata dai dati?
