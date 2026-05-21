<?php
declare(strict_types=1);

// Facade pattern example in PHP

class Auth
{
    public function authenticate(string $user): bool
    {
        return $user === 'alice';
    }
}

class Logger
{
    public function log(string $message): void
    {
        echo "[LOG] $message\n";
    }
}

class Payment
{
    public function charge(string $user, float $amount): bool
    {
        echo "Charging $user \$$amount\n";
        return true;
    }
}

class Notifier
{
    public function send(string $user, string $message): void
    {
        echo "Notify $user: $message\n";
    }
}

class ShopFacade
{
    private Auth $auth;
    private Logger $logger;
    private Payment $payment;
    private Notifier $notifier;

    public function __construct()
    {
        $this->auth = new Auth();
        $this->logger = new Logger();
        $this->payment = new Payment();
        $this->notifier = new Notifier();
    }

    public function purchase(string $user, float $amount): bool
    {
        $this->logger->log("Purchase started for $user");

        if (! $this->auth->authenticate($user)) {
            $this->logger->log("Authentication failed for $user");
            return false;
        }

        $paid = $this->payment->charge($user, $amount);
        if (! $paid) {
            $this->logger->log("Payment failed for $user");
            return false;
        }

        $this->notifier->send($user, "Your payment of \$$amount is successful.");
        $this->logger->log("Purchase completed for $user");
        return true;
    }
}

// Demo
$facade = new ShopFacade();
$facade->purchase('alice', 29.99);
$facade->purchase('bob', 19.99);
