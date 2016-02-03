#ifndef STOPWATCH_H
#define STOPWATCH_H

#include <QTimer>
#include <QWidget>

namespace Ui {
class Stopwatch;
}

class Stopwatch : public QWidget
{
    Q_OBJECT

public:
    explicit Stopwatch(QWidget *parent = 0);
    ~Stopwatch();

private slots:
    void countUp();
    void on_startButton_clicked();
    void on_clearButton_clicked();

private:
    Ui::Stopwatch *ui;
    QTimer* m_timer;
    int m_count;
};

#endif // STOPWATCH_H
