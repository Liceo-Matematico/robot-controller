
#include <Servo.h>

// ======================================
// PIN ROBOT
// ======================================

#define ML_Ctrl 13
#define ML_PWM 11

#define MR_Ctrl 12
#define MR_PWM 3

#define Trig 5
#define Echo 4

#define servoPin 9

// ======================================
// SERVO
// ======================================

Servo testa;

// ======================================
// STATO MOTORI
// ======================================

float velSX = 0;
float velDX = 0;

// ======================================
// SETUP
// ======================================

void setup() {

  Serial.begin(9600);

  pinMode(ML_Ctrl, OUTPUT);
  pinMode(ML_PWM, OUTPUT);

  pinMode(MR_Ctrl, OUTPUT);
  pinMode(MR_PWM, OUTPUT);

  pinMode(Trig, OUTPUT);
  pinMode(Echo, INPUT);

  testa.attach(servoPin);

  stopRobot();

  Serial.println("READY");
}

// ======================================
// LOOP
// ======================================

void loop() {

  // ----------------------------------
  // LEGGI COMANDI DA JAVA
  // ----------------------------------

  if (Serial.available()) {

    String line =
      Serial.readStringUntil('\n');

    line.trim();

    int comma = line.indexOf(',');

    if (comma > 0) {

      velSX =
        line.substring(0, comma).toFloat();

      velDX =
        line.substring(comma + 1).toFloat();

      setMotoreSX(velSX);

      setMotoreDX(velDX);
    }
  }

  // ----------------------------------
  // INVIA SCAN
  // ----------------------------------

  inviaScan();

  delay(200);
}

// ======================================
// MOTORE SX
// ======================================

void setMotoreSX(float v) {

  int pwm = abs(v) * 255;

  if (v >= 0) {

    // AVANTI

    digitalWrite(ML_Ctrl, LOW);

  } else {

    // INDIETRO

    digitalWrite(ML_Ctrl, HIGH);
  }

  analogWrite(ML_PWM, pwm);
}

// ======================================
// MOTORE DX
// ======================================

void setMotoreDX(float v) {

  int pwm = abs(v) * 255;

  if (v >= 0) {

    // AVANTI

    digitalWrite(MR_Ctrl, LOW);

  } else {

    // INDIETRO

    digitalWrite(MR_Ctrl, HIGH);
  }

  analogWrite(MR_PWM, pwm);
}

// ======================================
// STOP ROBOT
// ======================================

void stopRobot() {

  analogWrite(ML_PWM, 0);

  analogWrite(MR_PWM, 0);
}

// ======================================
// SCAN AMBIENTE
// ======================================

void inviaScan() {

  int d0   = misura(0);
  int d45  = misura(45);
  int d90  = misura(90);
  int d135 = misura(135);
  int d180 = misura(180);

  Serial.print(d0);
  Serial.print(",");

  Serial.print(d45);
  Serial.print(",");

  Serial.print(d90);
  Serial.print(",");

  Serial.print(d135);
  Serial.print(",");

  Serial.println(d180);
}

// ======================================
// MISURA DISTANZA
// ======================================

int misura(int angolo) {

  testa.write(angolo);

  delay(120);

  // trigger OFF
  digitalWrite(Trig, LOW);
  delayMicroseconds(2);

  // trigger ON
  digitalWrite(Trig, HIGH);
  delayMicroseconds(10);

  digitalWrite(Trig, LOW);

  long durata =
    pulseIn(Echo, HIGH);

  int distanza =
    durata * 0.034 / 2;

  // nessun echo
  if (distanza <= 0) {

    distanza = 999;
  }

  return distanza;
}


