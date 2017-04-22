# Funkcjonalność aplikacji:
1. Wykrywanie obecności czujnika w okolicy i automatyczne nawiązywanie z nim połączenia.
2. Odbiór danych z urządzenia (stężenie PM2.5, PM10, temperatura, ciśnienie, wilgotność) przesyłanych przez BT 4.0
3. Dołączenie do danych współrzędnych GPS i wysyłanie ich do serwisu ThinkSpeak oraz na mapę przygotowaną przez Tieto (poprzez API HTTP).
4. Wyświetlanie mapy i danych odebranych z innych czujników na niej.
5. Możliwość ustalenia przy pierwszym uruchomieniu i/lub w ustawieniach aplikacji klucza do API serwisu ThingSpeak.
6. W przypadku braku dostępu do Internetu dane będą przechowywanie lokalnie w postaci listy, początkowo w pamięci aplikacji, po wyłączeniu aplikacji - normalnie lub awaryjnie, wszystkie dane zostaną zapisane w pamięci wewnętrznej telefonu. Przy następnym uruchomieniu dane te zostaną wczytane, a po nawiązaniu łączności z Internetem, wysłane na serwer.
