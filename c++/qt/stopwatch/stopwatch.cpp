#include "stopwatch.h"
#include "ui_stopwatch.h"
#include <QTime>

Stopwatch::Stopwatch(QWidget *parent) :
    QWidget(parent),
    ui(new Ui::Stopwatch),
    m_timer(new QTimer(this)),
    m_count(0)
{
    ui->setupUi(this);
    connect(m_timer, SIGNAL(timeout()), this, SLOT(countUp()));
    on_clearButton_clicked();
}

Stopwatch::~Stopwatch()
{
    delete ui;
    delete m_timer;
}

void Stopwatch::countUp()
{
    m_count++;
    QTime t(0, 0);
    QTime time = t.addMSecs(m_count * m_timer->interval());
    ui->lcdNumber->display(time.toString("mm:ss.zzz"));
    update();
}

void Stopwatch::on_startButton_clicked()
{
    if (m_timer->isActive()) {
        m_timer->stop();
        ui->startButton->setText("Start");
    } else {
        m_timer->start(10);
        ui->startButton->setText("Stop");
    }
}

void Stopwatch::on_clearButton_clicked()
{
    m_timer->stop();
    m_count = 0;
    ui->lcdNumber->display("00:00.000");
    ui->startButton->setText("Start");
}
