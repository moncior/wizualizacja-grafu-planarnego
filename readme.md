# Wizualizacja grafu planarnego

## Opis zadania projektowego

Zadanie polega na stworzeniu aplikacji która ma wyznaczać współrzędne węzłów dla "ładnej" wizualizacji grafu planarnego podanego w postaci listy krawędzi.

## Użycie

### Podstawowe wywołanie

```bash
./program <plik_wejsciowy> -a <algorytm> [opcje]
```

### Opcje

- `-a <algorytm>` - wybór algorytmu (wymagane)
  - `fruch_rein` - Algorytm Fruchtermana-Reingolda
  - `eades` - Algorytm Eadesa
  - `tutte` - Algorytm Tutte

- `-o <plik_wyjściowy>` - plik wyjściowy (domyślnie stdout)

- `-f <format>` - format wyjściowy (domyślnie txt)

- `-v` - tryb verbose (szczegółowe informacje)

- `-h` - wyświetlenie pomocy

### Przykłady

1. **Podstawowe użycie z algorytmem Fruchtermana-Reingolda:**
   ```bash
   ./program tests/input/graph_1.in -a fruch_rein -o output.txt
   ```

2. **Z trybem verbose:**
   ```bash
   ./program tests/input/graph_1.in -a eades -v -o output.txt
   ```

3. **Wizualizacja wyniku:**
   ```bash
   python3 scripts/visualize.py tests/input/graph_1.in output.txt
   ```

## Format plików

### Plik wejściowy (lista krawędzi)

Format: `<węzeł1><węzeł2> <x1> <y1> <x2> <y2>`

Przykład:
```
AB 1 2 1
BC 2 3 1
CD 3 4 1
DB 4 2 1.407
```

### Plik wyjściowy (współrzędne węzłów)

Format: `<węzeł> <x> <y>`

Przykład:
```
1 72.0 81.0
2 77.0 40.0
3 93.0 35.0
4 86.0 92.0
```

## Testowanie

Projekt zawiera zestaw testów z przykładowymi grafami.

### Uruchomienie pojedynczego testu

```bash
make test alg=<algorytm> idx=<numer_testu>
```

Przykład:
```bash
make test alg=fruch_rein idx=3
```

### Uruchomienie wszystkich testów

```bash
make test_all
```

To polecenie uruchomi wszystkie kombinacje algorytmów i testów, generując wizualizacje w `tests/images/`.

## Algorytmy

### Fruchterman-Reingold

Algorytm siłowy, który modeluje węzły jako cząsteczki oddziałujące siłami przyciągania i odpychania. Daje naturalne rozmieszczenie z równomiernym rozłożeniem węzłów.

### Eades

Uproszczona wersja algorytmu siłowego, skupiająca się na minimalizacji przecięć krawędzi i optymalizacji kątów.

### Tutte

Algorytm który umieszcza wierzchołki znalezionego cyklu w grafie na zewnątrz, natomiast wewnętrzne wierzchołki w geometrycznym środku sąsiadów. Dzięki temu graf układa się przejrzyście i naturalnie, minimalizując przy tym ilość przeciętych krawędzi. 

## Przykład wizualizacji

![Przykład wizualizacji](assets/figure_1.png)

*Powyższy obraz przedstawia wynik działania algorytmu na przykładowym grafie.*

## Czyszczenie

Aby usunąć pliki tymczasowe i zbudowane artefakty:

```bash
make clean
```
