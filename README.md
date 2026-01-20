[![Coverage Status](https://coveralls.io/repos/github/lennyx24/Thirty-One/badge.svg?branch=master)](https://coveralls.io/github/lennyx24/Thirty-One?branch=master)

AIN Semester 3 HTWG, project in software engineering by Daniel Sauer and Lenny Jung

## Schnellstart

```bash
git clone https://github.com/lennyx24/Thirty-One.git
cd Thirty-One
sbt run
```

## Spielanleitung

  - Ziel: Erreiche 31 Punkte mit drei Handkarten (gleiche Farbe zählt am meisten).
  - Start: Jeder bekommt 3 Karten, in der Mitte liegen 3 Karten zum Tauschen.
  - Zug: Du kannst
      - Passen (nichts tun),
      - Tauschen (eine oder alle Handkarten gegen Mitte),
      - Klopfen (letzte Runde einläuten).
  - Rundenende: Nach dem Klopfen haben alle noch genau einen Zug.
  - Gewinner: Wer 31 hat oder am Ende die meisten Punkte.

  ## Punktewertung

  - Nur eine Farbe zählt: Du darfst nur die beste Farbe in deiner Hand werten.
  - Zahlenkarten: zählen ihren Zahlenwert (z. B. 7 = 7 Punkte).
  - Bilderkarten (B, D, K): zählen je 10 Punkte.
  - Ass: zählt 11 Punkte.
  - Drei gleiche Karten (Drilling): zählen 30,5 Punkte (beste Hand außer 31).

  Beispiel: Herz‑A, Herz‑K, Kreuz‑10 → zählt 11 + 10 = 21 (nur Herz‑Farbe).

## Docker

  ### Build
  docker build -t thirty-one:latest .

  ### Windows
  X-Server: VcXsrv oder Xming (VcXsrv empfohlen)
  1. VcXsrv starten (Multiple windows, Display 0).
  2. In VcXsrv: "Disable access control" aktivieren.
  3. Run:
  docker run --rm -it -e DISPLAY=host.docker.internal:0 thirty-one:latest

  ### Linux
  X-Server: der lokale X-Server (Xorg/Wayland mit Xwayland)
  1. Zugriff erlauben:
  xhost +local:root
  2. Run:
  docker run --rm -it -e DISPLAY=$DISPLAY -v /tmp/.X11-unix:/tmp/.X11-unix thirty-one:latest

  ### macOS
  1. XQuartz starten.
  2. Allow connections from network clients.
  3. xhost + 127.0.0.1
  4. Run:
  docker run --rm -it -e DISPLAY=host.docker.internal:0 -v /tmp/.X11-unix:/tmp/.X11-unix thirty-one:latest

### Troubleshooting (schwarzes Fenster/Buttons)

Wenn die GUI in XQuartz komplett schwarz ist, hilft oft das Abschalten von XRender/OpenGL:

```bash
docker run --rm -it \
  -e DISPLAY=host.docker.internal:0 \
  -e JAVA_TOOL_OPTIONS="-Dsun.java2d.xrender=false -Dsun.java2d.opengl=false" \
  -v /tmp/.X11-unix:/tmp/.X11-unix \
  thirty-one:latest
```
