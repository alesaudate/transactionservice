# 3. Balances without floating points

Date: 2022-02-26

## Status
 
Accepted

## Context

In order to avoid as much as possible conflicts with floating-point operations (such as divisions),
we should not use floating-point structures to store such values.

## Decision

We will use Long's to represent monetary amounts.

## Consequences

Whenever we apply some division, we must remember not to use only the result of division itself, but the remainder as well.