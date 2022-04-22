# 4. Hide transferred amounts

Date: 2022-02-26

## Status

Accepted

## Context

We must avoid logging transferred amounts from one account to another, in order to avoid conflicts with the current law concerning data processing.

## Decision

We will not log amounts and, if possible, the accounts to which they relate to. 

## Consequences

We will not have any access at all to attempts of transferring any balances, possibly impacting our ability to troubleshoot whenever needed. 