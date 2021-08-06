<?php

namespace OpenHealthhub\PhpFhirClient\Test;

use OpenHealthhub\PhpFhirClient\AppointmentClient;
use PHPUnit\Framework\TestCase;

class ClientTest extends TestCase
{
    public function testCanConstructTypeNoArgs()
    {
        $client = new AppointmentClient();
        $resp = $client->getAppointment(4);
        $this->assertNotNull($resp);
        $this->assertEquals('4', $resp->getId()->getValue()->getValue());
    }
}
